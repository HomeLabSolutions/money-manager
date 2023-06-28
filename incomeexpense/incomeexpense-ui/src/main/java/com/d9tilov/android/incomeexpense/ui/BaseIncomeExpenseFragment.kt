package com.d9tilov.android.incomeexpense.ui

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import androidx.viewbinding.ViewBinding
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.ui.recycler.CategoryAdapter
import com.d9tilov.android.designsystem.databinding.LayoutEmptyListPlaceholderBinding
import com.d9tilov.android.common_android.ui.base.BaseFragment
import com.d9tilov.android.common_android.ui.base.Inflate
import com.d9tilov.android.common_android.utils.gone
import com.d9tilov.android.common_android.utils.show
import com.d9tilov.android.common_android.utils.showWithAnimation
import com.d9tilov.android.common_android.utils.toast
import com.d9tilov.android.core.constants.NavigationConstants.ARG_TRANSACTION_CREATED
import com.d9tilov.android.core.events.OnDialogDismissListener
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.model.isIncome
import com.d9tilov.android.incomeexpense.navigation.BaseIncomeExpenseNavigator
import com.d9tilov.android.incomeexpense.ui.listeners.OnIncomeExpenseListener
import com.d9tilov.android.incomeexpense_ui.R
import com.d9tilov.android.incomeexpense.ui.vm.BaseIncomeExpenseViewModel
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.ui.TransactionAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.math.BigDecimal

abstract class BaseIncomeExpenseFragment<N : BaseIncomeExpenseNavigator, VB : ViewBinding>(
    inflate: Inflate<VB>,
    @LayoutRes layoutId: Int
) : BaseFragment<N, VB>(inflate, layoutId),
    OnDialogDismissListener,
    BaseIncomeExpenseNavigator,
    OnIncomeExpenseListener {

    protected val categoryAdapter =
        CategoryAdapter { item, _ ->
            if (item.id == Category.ALL_ITEMS_ID) (viewModel as BaseIncomeExpenseViewModel<N>).openAllCategories()
            else saveTransaction(item)
        }
    protected val transactionAdapter = TransactionAdapter(
        itemClickListener = { item, _ ->
            val action = IncomeExpenseFragmentDirections.toEditTransactionDest(item.type, item)
            findNavController().navigate(action)
        },
        itemSwipeListener = { item, _ -> openRemoveConfirmationDialog(item) }
    )

    private var isTransactionDataEmpty = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initCategoryRecyclerView()
        initTransactionsRecyclerView()
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Category>(ARG_TRANSACTION_CREATED)
            ?.observe(viewLifecycleOwner) {
            saveTransaction(it)
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Category>(ARG_TRANSACTION_CREATED)
        }
        transactionRvList?.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                private val fab = transactionBtnAdd
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0 || dy < 0 && fab?.isShown == true) fab?.hide()
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) fab?.show()
                    super.onScrollStateChanged(recyclerView, newState)
                }
            }
        )
        transactionBtnAdd?.setOnClickListener { (requireParentFragment() as IncomeExpenseFragment).openKeyboard() }

        // need for work horizontal scroll of categories rv and viewpager
        categoryRvList?.addOnItemTouchListener(object : OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(true)
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    private fun showViewStub() {
        if ((requireParentFragment() as IncomeExpenseFragment).isKeyboardOpened()) return
        emptyViewStub?.root?.show()
        emptyViewStub?.emptyPlaceholderIcon?.setImageDrawable(
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_wallet_empty)
        )
        emptyViewStub?.emptyPlaceholderTitle?.text =
            getString(
                if (getType().isIncome()) R.string.transaction_empty_placeholder_income_title
                else R.string.transaction_empty_placeholder_expense_title
            )
        emptyViewStub?.emptyPlaceholderSubtitle?.show()
        emptyViewStub?.emptyPlaceholderSubtitle?.text =
            getString(R.string.transaction_empty_placeholder_subtitle)
    }

    private fun hideViewStub() {
        emptyViewStub?.root?.gone()
    }

    private fun openRemoveConfirmationDialog(transaction: Transaction) {
        val action = IncomeExpenseFragmentDirections.toRemoveTransactionDialog(transaction)
        findNavController().navigate(action)
    }

    override fun showEmptySumError() {
        requireContext().toast(R.string.income_expense_empty_sum_error)
    }

    override fun onDismiss() {
        transactionAdapter.cancelDeletion()
    }

    override fun onKeyboardShown(show: Boolean) {
        if (show) {
            hideViewStub()
            if (isPremium()) infoLayout?.showWithAnimation()
            categoryRvList?.showWithAnimation()
            transactionsLayout?.gone()
        } else {
            if (isTransactionDataEmpty) showViewStub()
            transactionsLayout?.showWithAnimation()
            infoLayout?.gone()
            categoryRvList?.gone()
        }
    }

    private fun isPremium(): Boolean = (requireParentFragment() as IncomeExpenseFragment).isPremium
    protected fun isKeyboardOpen(): Boolean =
        (requireParentFragment() as IncomeExpenseFragment).isKeyboardOpen

    protected fun getSum(): BigDecimal = (requireParentFragment() as IncomeExpenseFragment).getSum()
    protected fun getCurrencyCode(): String =
        (requireParentFragment() as IncomeExpenseFragment).getCurrencyCode()

    private fun resetMainSum() {
        (requireParentFragment() as IncomeExpenseFragment).resetSum()
    }

    protected open var emptyViewStub: LayoutEmptyListPlaceholderBinding? = null
    protected open var categoryRvList: RecyclerView? = null
    protected open var transactionRvList: RecyclerView? = null
    protected open var transactionBtnAdd: FloatingActionButton? = null
    protected open var infoLayout: ConstraintLayout? = null
    protected open var transactionsLayout: ConstraintLayout? = null
    protected abstract fun getType(): TransactionType
    protected abstract fun initViews()
    protected abstract fun initCategoryRecyclerView()
    protected abstract fun initTransactionsRecyclerView()
    protected abstract fun saveTransaction(category: Category)

    companion object {
        const val SPAN_COUNT = 2
        const val TABLET_SPAN_COUNT = 1
    }
}