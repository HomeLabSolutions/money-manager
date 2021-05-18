package com.d9tilov.moneymanager.incomeexpense.expense.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.callback.SwipeToDeleteCallback
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.events.OnKeyboardVisibleChange
import com.d9tilov.moneymanager.core.ui.recyclerview.GridSpaceItemDecoration
import com.d9tilov.moneymanager.core.ui.recyclerview.ItemSnapHelper
import com.d9tilov.moneymanager.core.ui.recyclerview.StickyHeaderItemDecorator
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.core.util.isTablet
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.databinding.FragmentExpenseBinding
import com.d9tilov.moneymanager.databinding.LayoutExpenseInputFieldBinding
import com.d9tilov.moneymanager.home.ui.MainActivity
import com.d9tilov.moneymanager.incomeexpense.ui.BaseIncomeExpenseFragment
import com.d9tilov.moneymanager.incomeexpense.ui.IncomeExpenseFragmentDirections
import com.d9tilov.moneymanager.transaction.TransactionType
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

@AndroidEntryPoint
class ExpenseFragment :
    BaseIncomeExpenseFragment<ExpenseNavigator>(R.layout.fragment_expense),
    ExpenseNavigator,
    OnKeyboardVisibleChange {

    private val viewBinding by viewBinding(FragmentExpenseBinding::bind)
    private lateinit var mergeLayoutBinding: LayoutExpenseInputFieldBinding

    override fun getNavigator() = this
    override val viewModel by viewModels<ExpenseViewModel>()
    override val snackBarAnchorView by lazy { viewBinding.expenseCategoryRvList }

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mergeLayoutBinding = LayoutExpenseInputFieldBinding.bind(viewBinding.root)
        emptyViewStub = viewBinding.root.findViewById(R.id.expense_transaction_empty_placeholder)
        mergeLayoutBinding.run {
            expenseMainSum.moneyEditText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    expenseMainSum.moneyEditText.post {
                        expenseMainSum.moneyEditText.setSelection(expenseMainSum.moneyEditText.text.toString().length)
                    }
                }
                mainSum = expenseMainSum
            }
        }
        viewModel.run {
            categories.observe(
                viewLifecycleOwner,
                { list ->
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
            getTransactionEvent().observe(
                viewLifecycleOwner,
                {
                    hideKeyboard()
                    resetMainSum()
                }
            )
        }
        lifecycleScope.launch {
            viewModel.transactions.collectLatest {
                transactionAdapter.submitData(
                    it
                )
            }
        }
        lifecycleScope.launch {
            transactionAdapter
                .loadStateFlow
                .collectLatest { loadStates ->
                    isTransactionDataEmpty =
                        loadStates.source.refresh is LoadState.NotLoading && loadStates.append.endOfPaginationReached && transactionAdapter.itemCount == 0
                    if (isTransactionDataEmpty && !(activity as MainActivity).forceShowKeyboard) {
                        showViewStub(TransactionType.EXPENSE)
                    } else {
                        hideViewStub()
                    }
                }
        }
        viewBinding.expenseTransactionRvList.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            private val fab = viewBinding.expenseTransactionBtnAdd
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && fab.isShown) {
                    fab.hide()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        viewBinding.expenseTransactionBtnAdd.setOnClickListener {
            viewBinding.expenseTransactionRvList.gone()
            viewBinding.expenseTransactionBtnAdd.gone()
            changeInputFieldVisibility(true)
            mergeLayoutBinding.expenseMainSum.moneyEditText.requestFocus()
            showKeyboard(mergeLayoutBinding.expenseMainSum.moneyEditText)
        }
    }

    override fun onStart() {
        super.onStart()
        if ((activity as MainActivity).forceShowKeyboard) {
            showKeyboard(mergeLayoutBinding.expenseMainSum.moneyEditText)
        } else {
            viewBinding.expenseCategoryRvList.gone()
            changeInputFieldVisibility(false)
            viewBinding.expenseTransactionRvList.show()
            viewBinding.expenseTransactionBtnAdd.show()
        }
    }

    override fun initCategoryRecyclerView() {
        viewBinding.run {
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
            val snapHelper: SnapHelper = ItemSnapHelper()
            snapHelper.attachToRecyclerView(expenseCategoryRvList)
        }
    }

    override fun initTransactionsRecyclerView() {
        viewBinding.run {
            expenseTransactionRvList.layoutManager =
                LinearLayoutManager(requireContext())
            expenseTransactionRvList.adapter = transactionAdapter
            val itemDecoration = StickyHeaderItemDecorator(transactionAdapter)
            itemDecoration.attachToRecyclerView(expenseTransactionRvList)
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    transactionAdapter.deleteItem(viewHolder.bindingAdapterPosition)
                }
            }).attachToRecyclerView(expenseTransactionRvList)
        }
    }

    override fun openCategoriesScreen() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param(FirebaseAnalytics.Param.ITEM_ID, "click_all_categories_expense")
        }
        viewBinding.run {
            val inputSum = mergeLayoutBinding.expenseMainSum.getValue()
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

    override fun onOpenKeyboard() {
        Timber.tag(App.TAG).d("Keyboard shown")
        isKeyboardOpen = true
        viewBinding.run {
            onKeyboardVisibilityAnimation(true)
            expenseTransactionRvList.gone()
            expenseTransactionBtnAdd.gone()
            hideViewStub()
            expenseCategoryRvList.scrollToPosition(0)
        }
    }

    override fun onCloseKeyboard() {
        Timber.tag(App.TAG).d("Keyboard hidden")
        isKeyboardOpen = false
        viewBinding.run {
            onKeyboardVisibilityAnimation(false)
            if (isTransactionDataEmpty) {
                showViewStub(TransactionType.EXPENSE)
            }
            mergeLayoutBinding.expenseMainSum.clearFocus()
        }
    }

    private fun onKeyboardVisibilityAnimation(open: Boolean) {
        viewBinding.run {
            if (open) {
                val shownCategories = expenseCategoryRvList.show()
                if (shownCategories) {
                    animateCategories(open)
                }
                val showInputField = changeInputFieldVisibility(true)
                if (showInputField) {
                    animateInputField(open)
                }
            } else {
                changeInputFieldVisibility(false)
                val shownTransactions = expenseTransactionRvList.show()
                if (shownTransactions) {
                    animateTransactions(open)
                }
                expenseTransactionBtnAdd.show()
                val goneCategories = expenseCategoryRvList.gone()
                if (goneCategories) {
                    animateCategories(open)
                }
            }
        }
    }

    private fun changeInputFieldVisibility(isVisible: Boolean): Boolean {
        return mergeLayoutBinding.run {
            when (isVisible) {
                true -> {
                    expenseMainSum.show()
                    return@run expenseMainSumTitle.show()
                }
                false -> {
                    expenseMainSum.gone()
                    return@run expenseMainSumTitle.gone()
                }
            }
        }
    }

    private fun animateCategories(open: Boolean) {
        val alphaAnimationCategories =
            ObjectAnimator.ofFloat(
                viewBinding.expenseCategoryRvList,
                View.ALPHA,
                if (open) ALPHA_CAT_MIN else ALPHA_MAX,
                if (open) ALPHA_MAX else ALPHA_CAT_MIN
            ).apply {
                duration = ANIMATION_DURATION_CAT
                interpolator = AccelerateInterpolator()
            }
        alphaAnimationCategories.start()
    }

    private fun animateInputField(open: Boolean) {
        val alphaAnimationInputField =
            ObjectAnimator.ofFloat(
                mergeLayoutBinding.root,
                View.ALPHA,
                if (open) ALPHA_CAT_MIN else ALPHA_MAX,
                if (open) ALPHA_MAX else ALPHA_CAT_MIN
            ).apply {
                duration = ANIMATION_DURATION_CAT
                interpolator = AccelerateInterpolator()
            }
        alphaAnimationInputField.start()
    }

    private fun animateTransactions(open: Boolean) {
        val alphaAnimationTransactions =
            ObjectAnimator.ofFloat(
                viewBinding.expenseTransactionRvList,
                View.ALPHA,
                if (open) ALPHA_MAX else ALPHA_CAT_MIN,
                if (open) ALPHA_CAT_MIN else ALPHA_MAX
            ).apply {
                duration = ANIMATION_DURATION_TRANSACTION
                interpolator = AccelerateInterpolator()
            }
        alphaAnimationTransactions.start()
    }

    override fun saveTransaction(category: Category) {
        viewModel.saveTransaction(
            category,
            mergeLayoutBinding.expenseMainSum.getValue()
        )
        isTransactionDataEmpty = false
    }

    override fun resetMainSum() {
        mergeLayoutBinding.expenseMainSum.setValue(BigDecimal.ZERO)
    }

    companion object {
        fun newInstance() = ExpenseFragment()
    }
}
