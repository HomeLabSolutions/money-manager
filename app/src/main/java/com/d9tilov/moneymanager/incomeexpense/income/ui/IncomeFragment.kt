package com.d9tilov.moneymanager.incomeexpense.income.ui

import android.os.Bundle
import android.view.View
import android.view.ViewStub
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
import com.d9tilov.moneymanager.base.ui.navigator.IncomeNavigator
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
import com.d9tilov.moneymanager.databinding.FragmentIncomeBinding
import com.d9tilov.moneymanager.home.ui.MainActivity
import com.d9tilov.moneymanager.incomeexpense.ui.IncomeExpenseFragment
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
class IncomeFragment :
    BaseFragment<FragmentIncomeBinding, IncomeNavigator>(R.layout.fragment_income),
    IncomeNavigator,
    OnKeyboardVisibleChange,
    OnDialogDismissListener {

    private val categoryAdapter = CategoryAdapter()
    private val transactionAdapter = TransactionAdapter()
    override fun performDataBinding(view: View): FragmentIncomeBinding =
        FragmentIncomeBinding.bind(view)

    override fun getNavigator() = this
    override val viewModel by viewModels<IncomeViewModel>()
    override val snackBarBackgroundTint = R.color.button_normal_color_disable
    override val snackBarAnchorView by lazy { viewBinding?.incomeCategoryRvList }

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    private var isTransactionDataEmpty = false
    private lateinit var viewStub: ViewStub

    private val onAllCategoriesClickListener = object : OnItemClickListener<Category> {
        override fun onItemClick(item: Category, position: Int) {
            if (item.id == Category.ALL_ITEMS_ID) {
                viewModel.openAllCategories()
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                    param(FirebaseAnalytics.Param.SCREEN_NAME, "income")
                    param(FirebaseAnalytics.Param.ITEM_ID, "click_all_categories")
                }
            } else {
                viewBinding?.let {
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                        param(FirebaseAnalytics.Param.SCREEN_NAME, "income")
                        param(FirebaseAnalytics.Param.ITEM_ID, "click_category")
                        param(
                            FirebaseAnalytics.Param.METHOD,
                            if (it.incomeMainSum.getValue()
                                    .signum() > 0
                            ) "create_transaction" else "empty_click"
                        )
                    }
                    viewModel.saveTransaction(item, it.incomeMainSum.getValue())
                    it.incomeMainSum.setValue(BigDecimal.ZERO)
                }
            }
        }
    }

    private val onTransactionClickListener = object : OnItemClickListener<Transaction> {
        override fun onItemClick(item: Transaction, position: Int) {
            val action = IncomeExpenseFragmentDirections.toEditTransactionDest(
                item.type,
                item
            )
            findNavController().navigate(action)
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                param(FirebaseAnalytics.Param.SCREEN_NAME, "income")
                param(FirebaseAnalytics.Param.ITEM_ID, "click_transaction")
            }
        }
    }
    private val onItemSwipeListener = object : OnItemSwipeListener<Transaction> {
        override fun onItemSwiped(item: Transaction, position: Int) {
            viewModel.deleteTransaction(item)
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                param(FirebaseAnalytics.Param.SCREEN_NAME, "income")
                param(FirebaseAnalytics.Param.ITEM_ID, "swipe_transaction_to_remove")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter.itemClickListener = onAllCategoriesClickListener
        transactionAdapter.itemSwipeListener = onItemSwipeListener
        transactionAdapter.itemClickListener = onTransactionClickListener
    }

    override fun onStart() {
        super.onStart()
        if ((activity as MainActivity).forceShowKeyboard) {
            showKeyboard(viewBinding?.incomeMainSum)
        } else {
            viewBinding?.run {
                incomeTransactionRvList.visibility = View.VISIBLE
                incomeCategoryRvList.visibility = View.GONE
            }
        }
    }

    override fun onStop() {
        hideKeyboard()
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCategoryRecyclerView()
        initTransactionsRecyclerView()
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            IncomeExpenseFragment.ARG_TRANSACTION_CREATED
        )?.observe(viewLifecycleOwner) {
            if (it) {
                viewBinding?.incomeMainSum?.setValue(BigDecimal.ZERO)
            }
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>(
                IncomeExpenseFragment.ARG_TRANSACTION_CREATED
            )
        }
        viewBinding?.let {
            viewStub = it.root.findViewById(R.id.income_transaction_empty_placeholder)
            it.incomeMainSum.moneyEditText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    it.incomeMainSum.moneyEditText.post {
                        it.incomeMainSum.moneyEditText.setSelection(it.incomeMainSum.moneyEditText.text.toString().length)
                    }
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                        param(FirebaseAnalytics.Param.ITEM_ID, "click_sum")
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
                        viewStub.visibility = View.VISIBLE
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

    override fun onDismiss() {
        transactionAdapter.cancelDeletion()
    }

    private fun initCategoryRecyclerView() {
        viewBinding?.run {
            val layoutManager =
                GridLayoutManager(requireContext(), SPAN_COUNT, GridLayoutManager.HORIZONTAL, false)
            incomeCategoryRvList.layoutManager = layoutManager
            incomeCategoryRvList.adapter = categoryAdapter
            incomeCategoryRvList.addItemDecoration(
                GridSpaceItemDecoration(
                    SPAN_COUNT,
                    resources.getDimension(R.dimen.recycler_view_category_offset).toInt(),
                    layoutManager.orientation
                )
            )
            val snapHelper: SnapHelper =
                ItemSnapHelper()
            snapHelper.attachToRecyclerView(incomeCategoryRvList)
        }
    }

    private fun initTransactionsRecyclerView() {
        viewBinding?.run {
            incomeTransactionRvList.layoutManager =
                LinearLayoutManager(requireContext())
            incomeTransactionRvList.adapter = transactionAdapter
            val itemDecoration =
                StickyHeaderItemDecorator(
                    transactionAdapter
                )
            itemDecoration.attachToRecyclerView(incomeTransactionRvList)
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    transactionAdapter.deleteItem(viewHolder.adapterPosition)
                }
            }).attachToRecyclerView(incomeTransactionRvList)
        }
    }

    override fun openCategoriesScreen() {
        viewBinding?.let {
            val inputSum = it.incomeMainSum.getValue()
            val action = if (inputSum.signum() > 0) {
                IncomeExpenseFragmentDirections.toCategoryDest(
                    destination = CategoryDestination.MAIN_WITH_SUM_SCREEN,
                    sum = inputSum,
                    transactionType = TransactionType.INCOME
                )
            } else {
                IncomeExpenseFragmentDirections.toCategoryDest(
                    destination = CategoryDestination.MAIN_SCREEN,
                    transactionType = TransactionType.INCOME
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
        viewBinding?.run {
            incomeTransactionRvList.visibility = View.INVISIBLE
            incomeCategoryRvList.visibility = View.VISIBLE
            viewStub.visibility = View.GONE
            incomeCategoryRvList.scrollToPosition(0)
        }
    }

    override fun onCloseKeyboard() {
        Timber.tag(App.TAG).d("Kyboard hidden")
        viewBinding?.run {
            incomeTransactionRvList.visibility = View.VISIBLE
            incomeCategoryRvList.visibility = View.GONE
            if (isTransactionDataEmpty) {
                viewStub.visibility = View.VISIBLE
            }
            incomeMainSum.clearFocus()
        }
    }

    companion object {
        fun newInstance() = IncomeFragment()
        private const val SPAN_COUNT = 2
    }
}
