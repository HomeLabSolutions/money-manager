package com.d9tilov.moneymanager.incomeexpense.ui

import android.os.Bundle
import android.view.View
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.BaseIncomeExpenseNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.ui.recycler.CategoryAdapter
import com.d9tilov.moneymanager.core.events.OnDialogDismissListener
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.events.OnItemSwipeListener
import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyView
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.incomeexpense.ui.vm.BaseIncomeExpenseViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.ui.TransactionAdapter

abstract class BaseIncomeExpenseFragment<N : BaseIncomeExpenseNavigator>(@LayoutRes layoutId: Int) :
    BaseFragment<N>(layoutId), OnDialogDismissListener, BaseIncomeExpenseNavigator {

    protected open lateinit var mainSum: CurrencyView
    protected val categoryAdapter = CategoryAdapter()
    protected val transactionAdapter = TransactionAdapter()
    protected var isTransactionDataEmpty = false
    protected var isKeyboardOpen = true

    protected var emptyViewStub: ViewStub? = null
    override val snackBarBackgroundTint = R.color.button_normal_color_disable

    private val onAllCategoriesClickListener = object : OnItemClickListener<Category> {
        override fun onItemClick(item: Category, position: Int) {
            if (item.id == Category.ALL_ITEMS_ID) {
                (viewModel as BaseIncomeExpenseViewModel<N>).openAllCategories()
            } else {
                saveTransaction(item)
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
        }
    }
    private val onItemSwipeListener = object : OnItemSwipeListener<Transaction> {
        override fun onItemSwiped(item: Transaction, position: Int) {
            openRemoveConfirmationDialog(item)
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
            IncomeExpenseFragment.ARG_TRANSACTION_CREATED
        )?.observe(viewLifecycleOwner) {
            if (it) {
                resetMainSum()
            }
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>(
                IncomeExpenseFragment.ARG_TRANSACTION_CREATED
            )
        }
    }

    protected fun showViewStub(transactionType: TransactionType) {
        if (emptyViewStub?.parent == null) {
            emptyViewStub?.show()
        } else {
            val inflatedStub = emptyViewStub?.inflate()
            val stubIcon =
                inflatedStub?.findViewById<ImageView>(R.id.empty_placeholder_icon)
            stubIcon?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_wallet_empty
                )
            )
            val stubTitle =
                inflatedStub?.findViewById<TextView>(R.id.empty_placeholder_title)
            stubTitle?.text =
                getString(
                    if (transactionType == TransactionType.EXPENSE)
                        R.string.transaction_empty_placeholder_expense_title
                    else R.string.transaction_empty_placeholder_income_title
                )
            val stubSubTitle =
                inflatedStub?.findViewById<TextView>(R.id.empty_placeholder_subtitle)
            stubSubTitle?.show()
            stubSubTitle?.text = getString(R.string.transaction_empty_placeholder_subtitle)
            val addTransaction =
                inflatedStub?.findViewById<ImageView>(R.id.empty_placeholder_add)
            addTransaction?.setOnClickListener {
                showKeyboard(mainSum.moneyEditText)
            }
        }
    }

    protected fun hideViewStub() {
        emptyViewStub?.gone()
    }

    private fun openRemoveConfirmationDialog(transaction: Transaction) {
        val action = IncomeExpenseFragmentDirections.toRemoveTransactionDialog(
            transaction
        )
        findNavController().navigate(action)
    }

    override fun showEmptySumError() {
        showSnackBar(getString(R.string.income_expense_empty_sum_error), true)
    }

    override fun onStop() {
        hideKeyboard()
        super.onStop()
    }

    override fun onDismiss() {
        transactionAdapter.cancelDeletion()
    }

    protected abstract fun initCategoryRecyclerView()
    protected abstract fun initTransactionsRecyclerView()
    protected abstract fun saveTransaction(category: Category)
    protected abstract fun resetMainSum()

    companion object {
        const val SPAN_COUNT = 2
        const val TABLET_SPAN_COUNT = 1
        const val ANIMATION_DURATION_CAT = 400L
        const val ALPHA_CAT_MIN = 0f
        const val ALPHA_MAX = 1f
    }
}