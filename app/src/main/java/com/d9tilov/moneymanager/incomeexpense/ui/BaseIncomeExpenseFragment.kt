package com.d9tilov.moneymanager.incomeexpense.ui

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.BaseIncomeExpenseNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.ui.recycler.CategoryAdapter
import com.d9tilov.moneymanager.core.events.OnBackPressed
import com.d9tilov.moneymanager.core.events.OnDialogDismissListener
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.events.OnItemSwipeListener
import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyView
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.core.util.toast
import com.d9tilov.moneymanager.incomeexpense.ui.vm.BaseIncomeExpenseViewModel
import com.d9tilov.moneymanager.keyboard.PinButton
import com.d9tilov.moneymanager.keyboard.PinKeyboard
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.ui.TransactionAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseIncomeExpenseFragment<N : BaseIncomeExpenseNavigator>(@LayoutRes layoutId: Int) :
    BaseFragment<N>(layoutId), OnDialogDismissListener, BaseIncomeExpenseNavigator, OnBackPressed {

    protected val categoryAdapter by lazy { CategoryAdapter() }
    protected val transactionAdapter by lazy { TransactionAdapter() }
    protected var isTransactionDataEmpty = false
    protected var isKeyboardOpen = true

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
        btnHideKeyboard.setOnClickListener {
            isKeyboardOpen = false
            showInfoAndCategories()
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
        lifecycleScope.launch {
            transactionAdapter
                .loadStateFlow
                .collect { loadStates ->
                    val isDataEmpty =
                        loadStates.source.refresh is LoadState.NotLoading && loadStates.append.endOfPaginationReached && transactionAdapter.itemCount == 0
                    isTransactionDataEmpty = isDataEmpty
                    if (isTransactionDataEmpty) showViewStub() else hideViewStub()
                }
        }
        transactionRvList.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                private val fab = transactionBtnAdd
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0 || dy < 0 && fab.isShown) fab.hide()
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) fab.show()
                    super.onScrollStateChanged(recyclerView, newState)
                }
            }
        )
        transactionBtnAdd.setOnClickListener {
            isKeyboardOpen = true
            showInfoAndCategories()
        }
        pinKeyboard.clickPinButton = object : PinKeyboard.ClickPinButton {
            override fun onPinClick(button: PinButton?) {
                when (button?.id) {
                    R.id.keyboard_pin_0 -> handlePinStr(getString(R.string._0))
                    R.id.keyboard_pin_1 -> handlePinStr(getString(R.string._1))
                    R.id.keyboard_pin_2 -> handlePinStr(getString(R.string._2))
                    R.id.keyboard_pin_3 -> handlePinStr(getString(R.string._3))
                    R.id.keyboard_pin_4 -> handlePinStr(getString(R.string._4))
                    R.id.keyboard_pin_5 -> handlePinStr(getString(R.string._5))
                    R.id.keyboard_pin_6 -> handlePinStr(getString(R.string._6))
                    R.id.keyboard_pin_7 -> handlePinStr(getString(R.string._7))
                    R.id.keyboard_pin_8 -> handlePinStr(getString(R.string._8))
                    R.id.keyboard_pin_9 -> handlePinStr(getString(R.string._9))
                    R.id.keyboard_pin_dot -> handlePinStr(getString(R.string._dot))
                    else -> handlePinStr(null)
                }
            }
        }
        // need for work horizontal scroll of categories rv and viewpager
        categoryRvList.addOnItemTouchListener(object : OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(true)
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
        showInfoAndCategories()
    }

    private fun handlePinStr(str: String?) {
        val dot = getString(R.string._dot)
        val zero = getString(R.string._0)
        var input = mainSum.moneyEditText.text.toString()
        when (str) {
            dot -> {
                if (input.contains(dot)) return
                if (input.isEmpty() || input[input.length - 1].toString() == dot) return
                else input = input.plus(str)
            }
            zero -> {
                if (input.length == 1 && input[0].toString() == zero) return
                else input = input.plus(str)
            }
            null ->
                if (input.isNotEmpty()) input = input.removeRange(input.length - 1, input.length)
            else -> input = input.plus(str)
        }
        if (input.isEmpty()) input = getString(R.string._0)
        mainSum.moneyEditText.setText(input)
    }

    protected fun showInfoAndCategories() {
        val hiddenGroup = if (isKeyboardOpen) transactionsLayout else infoLayout
        val shownGroup = if (isKeyboardOpen) infoLayout else transactionsLayout
        if (isKeyboardOpen) {
            hideViewStub()
        } else {
            if (isTransactionDataEmpty) showViewStub()
        }
        shownGroup.apply {
            alpha = 0f
            show()
            animate()
                .alpha(1f)
                .setDuration(ANIMATION_DURATION)
                .setListener(null)
        }
        hiddenGroup.gone()
    }

    private fun showViewStub() {
        if (isKeyboardOpen) return
        if (emptyViewStub.parent == null) {
            emptyViewStub.show()
        } else {
            val inflatedStub = emptyViewStub.inflate()
            val stubIcon =
                inflatedStub?.findViewById<ImageView>(R.id.empty_placeholder_icon)
            stubIcon?.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_wallet_empty)
            )
            val stubTitle = inflatedStub?.findViewById<TextView>(R.id.empty_placeholder_title)
            stubTitle?.text =
                getString(
                    if (getType() == TransactionType.EXPENSE)
                        R.string.transaction_empty_placeholder_expense_title
                    else R.string.transaction_empty_placeholder_income_title
                )
            val stubSubTitle = inflatedStub?.findViewById<TextView>(R.id.empty_placeholder_subtitle)
            stubSubTitle?.show()
            stubSubTitle?.text = getString(R.string.transaction_empty_placeholder_subtitle)
            val addTransaction = inflatedStub?.findViewById<ImageView>(R.id.empty_placeholder_add)
            addTransaction?.setOnClickListener {
                isKeyboardOpen = true
                showInfoAndCategories()
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

    override fun onDismiss() {
        transactionAdapter.cancelDeletion()
    }

    override fun onBackPressed(): Boolean {
        return if (isKeyboardOpen) {
            isKeyboardOpen = false
            showInfoAndCategories()
            false
        } else {
            true
        }
    }

    protected open lateinit var emptyViewStub: ViewStub
    protected open lateinit var mainSum: CurrencyView
    protected open lateinit var mainSumTitle: TextView
    protected open lateinit var categoryRvList: RecyclerView
    protected open lateinit var transactionRvList: RecyclerView
    protected open lateinit var transactionBtnAdd: FloatingActionButton
    protected open lateinit var btnHideKeyboard: ImageView
    protected open lateinit var infoLayout: ConstraintLayout
    protected open lateinit var transactionsLayout: ConstraintLayout
    protected open lateinit var pinKeyboard: PinKeyboard
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
