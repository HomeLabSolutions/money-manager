package com.d9tilov.moneymanager.incomeexpense.expense.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewStub
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.callback.SwipeToDeleteCallback
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.ui.recycler.CategoryAdapter
import com.d9tilov.moneymanager.core.events.OnDialogDismissListener
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.events.OnItemSwipeListener
import com.d9tilov.moneymanager.core.events.OnKeyboardVisibleChange
import com.d9tilov.moneymanager.core.ui.recyclerview.GridSpaceItemDecoration
import com.d9tilov.moneymanager.core.ui.recyclerview.ItemSnapHelper
import com.d9tilov.moneymanager.core.ui.recyclerview.StickyHeaderItemDecorator
import com.d9tilov.moneymanager.core.util.isTablet
import com.d9tilov.moneymanager.databinding.FragmentExpenseBinding
import com.d9tilov.moneymanager.home.ui.MainActivity
import com.d9tilov.moneymanager.incomeexpense.ui.IncomeExpenseFragment.Companion.ARG_TRANSACTION_CREATED
import com.d9tilov.moneymanager.incomeexpense.ui.IncomeExpenseFragmentDirections
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.ui.TransactionAdapter
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.mfms.common.util.hideKeyboard
import com.mfms.common.util.showKeyboard
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

@AndroidEntryPoint
class ExpenseFragment :
    BaseFragment<FragmentExpenseBinding, ExpenseNavigator>(R.layout.fragment_expense),
    ExpenseNavigator,
    OnKeyboardVisibleChange,
    OnDialogDismissListener {

    private val categoryAdapter = CategoryAdapter()
    private val transactionAdapter = TransactionAdapter()
    override fun performDataBinding(view: View): FragmentExpenseBinding =
        FragmentExpenseBinding.bind(view)

    override fun getNavigator() = this
    override val viewModel by viewModels<ExpenseViewModel>()
    override val snackBarBackgroundTint = R.color.button_normal_color_disable
    override val snackBarAnchorView by lazy { viewBinding?.expenseCategoryRvList }

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    private var isTransactionDataEmpty = false
    private var isKeyboardOpen = true
    private lateinit var emptyViewStub: ViewStub
    private lateinit var infoViewStub: ViewStub
    private lateinit var infoInflatedView: View

    private val onAllCategoriesClickListener = object : OnItemClickListener<Category> {
        override fun onItemClick(item: Category, position: Int) {
            if (item.id == Category.ALL_ITEMS_ID) {
                viewModel.openAllCategories()
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                    param(FirebaseAnalytics.Param.ITEM_ID, "click_all_categories_expense")
                }
            } else {
                viewBinding?.let {
                    viewModel.saveTransaction(item, it.expenseMainSum.getValue())
                    it.expenseMainSum.setValue(BigDecimal.ZERO)
                }
            }
        }
    }

    private val onTransactionClickListener = object : OnItemClickListener<Transaction> {
        override fun onItemClick(item: Transaction, position: Int) {
            if (isKeyboardOpen) {
                return
            }
            val action = IncomeExpenseFragmentDirections.toEditTransactionDest(
                TransactionType.EXPENSE,
                item
            )
            findNavController().navigate(action)
        }
    }
    private val onItemSwipeListener = object : OnItemSwipeListener<Transaction> {
        override fun onItemSwiped(item: Transaction, position: Int) {
            viewModel.deleteTransaction(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter.itemClickListener = onAllCategoriesClickListener
        transactionAdapter.itemSwipeListener = onItemSwipeListener
        transactionAdapter.itemClickListener = onTransactionClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCategoryRecyclerView()
        initTransactionsRecyclerView()
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            ARG_TRANSACTION_CREATED
        )?.observe(viewLifecycleOwner) {
            if (it) {
                viewBinding?.expenseMainSum?.setValue(BigDecimal.ZERO)
            }
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>(
                ARG_TRANSACTION_CREATED
            )
        }
        viewBinding?.let {
            emptyViewStub = it.root.findViewById(R.id.expense_transaction_empty_placeholder)
            infoViewStub = it.root.findViewById(R.id.expense_info)
            it.expenseMainSum.moneyEditText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    it.expenseMainSum.moneyEditText.post {
                        it.expenseMainSum.moneyEditText.setSelection(it.expenseMainSum.moneyEditText.text.toString().length)
                    }
                }
            }
        }
        viewModel.run {
            categories.observe(
                viewLifecycleOwner,
                Observer { list ->
                    val sortedCategories = list.sortedWith(
                        compareBy(
                            { it.children.isEmpty() },
                            { -it.usageCount },
                            { it.name }
                        )
                    )
                    categoryAdapter.updateItems(sortedCategories)
                }
            )
            transactions.observe(
                viewLifecycleOwner,
                Observer {
                    isTransactionDataEmpty = it.isEmpty()
                    if (isTransactionDataEmpty && !(activity as MainActivity).forceShowKeyboard) {
                        emptyViewStub.visibility = VISIBLE
                    }
                    transactionAdapter.submitList(it)
                }
            )
            addTransactionEvent.observe(
                viewLifecycleOwner,
                Observer { hideKeyboard() }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        if ((activity as MainActivity).forceShowKeyboard) {
            infoInflatedView = infoViewStub.inflate()
            showKeyboard(viewBinding?.expenseMainSum)
        } else {
            viewBinding?.run {
                expenseTransactionRvList.visibility = VISIBLE
                expenseCategoryRvList.visibility = GONE
            }
        }
    }

    override fun onStop() {
        hideKeyboard()
        super.onStop()
    }

    override fun onDismiss() {
        transactionAdapter.cancelDeletion()
    }

    private fun initCategoryRecyclerView() {
        viewBinding?.run {
            val layoutManager =
                GridLayoutManager(
                    requireContext(),
                    if (requireContext().isTablet()) TABLET_SPAN_COUNT else SPAN_COUNT,
                    GridLayoutManager.HORIZONTAL,
                    false
                )
            expenseCategoryRvList.layoutManager = layoutManager
            expenseCategoryRvList.adapter = categoryAdapter
            expenseCategoryRvList.addItemDecoration(
                GridSpaceItemDecoration(
                    SPAN_COUNT,
                    resources.getDimension(R.dimen.recycler_view_category_offset).toInt(),
                    layoutManager.orientation
                )
            )
            val snapHelper: SnapHelper =
                ItemSnapHelper()
            snapHelper.attachToRecyclerView(expenseCategoryRvList)
        }
    }

    private fun initTransactionsRecyclerView() {
        viewBinding?.run {
            expenseTransactionRvList.layoutManager =
                LinearLayoutManager(requireContext())
            expenseTransactionRvList.adapter = transactionAdapter
            val itemDecoration =
                StickyHeaderItemDecorator(
                    transactionAdapter
                )
            itemDecoration.attachToRecyclerView(expenseTransactionRvList)
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    transactionAdapter.deleteItem(viewHolder.adapterPosition)
                }
            }).attachToRecyclerView(expenseTransactionRvList)
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    transactionAdapter.deleteItem(viewHolder.adapterPosition)
                }
            }).attachToRecyclerView(expenseTransactionRvList)
        }
    }

    override fun openCategoriesScreen() {
        viewBinding?.let {
            val inputSum = it.expenseMainSum.getValue()
            val action = if (inputSum.signum() > 0) {
                IncomeExpenseFragmentDirections.toCategoryDest(
                    destination = CategoryDestination.MAIN_WITH_SUM_SCREEN,
                    sum = inputSum,
                    transactionType = TransactionType.EXPENSE
                )
            } else {
                IncomeExpenseFragmentDirections.toCategoryDest(
                    destination = CategoryDestination.MAIN_SCREEN,
                    transactionType = TransactionType.EXPENSE
                )
            }
            findNavController().navigate(action)
        }
    }

    override fun openRemoveConfirmationDialog(transaction: Transaction) {
        val action = IncomeExpenseFragmentDirections.toRemoveTransactionDialog(
            transaction
        )
        findNavController().navigate(action)
    }

    override fun showEmptySumError() {
        showSnackBar(getString(R.string.income_expense_empty_sum_error), true)
    }

    override fun onOpenKeyboard() {
        Timber.tag(App.TAG).d("Keyboard shown")
        isKeyboardOpen = true
        viewBinding?.run {
            onKeyboardVisibilityAnimation(true)
            expenseTransactionRvList.visibility = GONE
            emptyViewStub.visibility = GONE
            infoViewStub.visibility = VISIBLE
            expenseCategoryRvList.scrollToPosition(0)
        }
    }

    override fun onCloseKeyboard() {
        Timber.tag(App.TAG).d("Kyboard hidden")
        isKeyboardOpen = false
        viewBinding?.run {
            onKeyboardVisibilityAnimation(false)
            if (isTransactionDataEmpty) {
                emptyViewStub.visibility = VISIBLE
            }
            expenseMainSum.clearFocus()
        }
    }

    private fun onKeyboardVisibilityAnimation(open: Boolean) {
        if (open) {
            viewBinding?.expenseCategoryRvList?.visibility = VISIBLE
        } else {
            viewBinding?.expenseTransactionRvList?.visibility = VISIBLE
            viewBinding?.expenseCategoryRvList?.visibility = GONE
        }

        val alphaAnimationCategories =
            ObjectAnimator.ofFloat(
                viewBinding?.expenseCategoryRvList,
                View.ALPHA,
                if (open) ALPHA_CAT_MIN else ALPHA_MAX,
                if (open) ALPHA_MAX else ALPHA_CAT_MIN
            ).apply {
                duration = ANIMATION_DURATION_CAT
                interpolator = AccelerateInterpolator()
            }
        alphaAnimationCategories.start()
    }

    companion object {
        fun newInstance() = ExpenseFragment()
        private const val SPAN_COUNT = 2
        private const val TABLET_SPAN_COUNT = 1
        private const val ANIMATION_DURATION_CAT = 400L
        private const val ALPHA_CAT_MIN = 0f
        private const val ALPHA_MAX = 1f
    }
}
