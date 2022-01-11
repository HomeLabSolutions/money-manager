package com.d9tilov.moneymanager.incomeexpense.ui

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
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
import com.d9tilov.moneymanager.core.util.toast
import com.d9tilov.moneymanager.home.ui.MainActivity
import com.d9tilov.moneymanager.incomeexpense.ui.vm.BaseIncomeExpenseViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.ui.TransactionAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

abstract class BaseIncomeExpenseFragment<N : BaseIncomeExpenseNavigator>(@LayoutRes layoutId: Int) :
    BaseFragment<N>(layoutId), OnDialogDismissListener, BaseIncomeExpenseNavigator {

    protected val categoryAdapter by lazy { CategoryAdapter() }
    protected val transactionAdapter by lazy { TransactionAdapter() }
    protected var isTransactionDataEmpty = false
    private var isKeyboardOpen = true
    private val categoryGroup = mutableListOf<View>()
    private val transactionGroup = mutableListOf<View>()

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
        initViews()
        initCategoryRecyclerView()
        initTransactionsRecyclerView()
        categoryGroup.run {
            add(categoryRvList)
            add(mainSum)
            add(mainSumTitle)
            add(infoLayout)
        }
        transactionGroup.run {
            add(transactionBtnAdd)
            add(transactionRvList)
        }
        categoryGroup.forEach { it.gone() }
        transactionGroup.forEach { it.gone() }
        mainSum.moneyEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                mainSum.moneyEditText.post {
                    mainSum.moneyEditText.setSelection(mainSum.moneyEditText.text.toString().length)
                }
            }
        }
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
        transactionRvList.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                private val fab = transactionBtnAdd
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
            }
        )
        transactionBtnAdd.setOnClickListener {
            mainSum.moneyEditText.requestFocus()
            showKeyboard(mainSum.moneyEditText)
        }
    }

    protected fun crossFade(openKeyboard: Boolean) {
        val hiddenGroup = if (openKeyboard) transactionGroup else categoryGroup
        val shownGroup = if (openKeyboard) categoryGroup else transactionGroup
        shownGroup.forEach {
            it.apply {
                alpha = 0f
                show()
                animate()
                    .alpha(1f)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(null)
            }
        }
        hiddenGroup.forEach { it.gone() }
    }

    protected fun onOpenKeyboardBase() {
        isKeyboardOpen = true
        crossFade(true)
        hideViewStub()
        categoryRvList.scrollToPosition(0)
    }

    protected fun onCloseKeyboardBase() {
        isKeyboardOpen = false
        crossFade(false)
        if (isTransactionDataEmpty) {
            showViewStub(TransactionType.EXPENSE)
        }
        mainSum.clearFocus()
    }

    protected fun showViewStub(transactionType: TransactionType) {
        if (emptyViewStub.parent == null) {
            emptyViewStub.show()
        } else {
            val inflatedStub = emptyViewStub.inflate()
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
        /*don't use gone() extension!!! isVisible and isShown not worked*/
        emptyViewStub.visibility = GONE
    }

    private fun openRemoveConfirmationDialog(transaction: Transaction) {
        val action = IncomeExpenseFragmentDirections.toRemoveTransactionDialog(
            transaction
        )
        findNavController().navigate(action)
    }

    override fun showEmptySumError() {
        requireContext().toast(R.string.income_expense_empty_sum_error)
    }

    override fun onStart() {
        super.onStart()
        if ((activity as MainActivity).forceShowKeyboard) {
            showKeyboard(mainSum.moneyEditText)
        }
    }

    override fun onStop() {
        hideKeyboard()
        super.onStop()
    }

    override fun onDismiss() {
        transactionAdapter.cancelDeletion()
    }

    protected open lateinit var emptyViewStub: ViewStub
    protected open lateinit var mainSum: CurrencyView
    protected open lateinit var mainSumTitle: TextView
    protected open lateinit var categoryRvList: RecyclerView
    protected open lateinit var transactionRvList: RecyclerView
    protected open lateinit var infoLayout: ConstraintLayout
    protected open lateinit var transactionBtnAdd: FloatingActionButton
    protected abstract fun getType(): TransactionType
    protected abstract fun initViews()
    protected abstract fun initCategoryRecyclerView()
    protected abstract fun initTransactionsRecyclerView()
    protected abstract fun saveTransaction(category: Category)
    protected abstract fun resetMainSum()

    companion object {
        const val SPAN_COUNT = 2
        const val TABLET_SPAN_COUNT = 1
        const val ANIMATION_DURATION = 300L
    }
}
