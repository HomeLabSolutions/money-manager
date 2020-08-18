package com.d9tilov.moneymanager.incomeexpense.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.IncomeExpenseNavigator
import com.d9tilov.moneymanager.core.events.OnDialogDismissListener
import com.d9tilov.moneymanager.core.events.OnKeyboardVisibleChange
import com.d9tilov.moneymanager.databinding.FragmentIncomeExpenseBinding
import com.d9tilov.moneymanager.incomeexpense.ui.adapter.IncomeExpenseAdapter
import com.d9tilov.moneymanager.incomeexpense.ui.vm.IncomeExpenseViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.ui.TransactionRemoveDialogFragment.Companion.ARG_UNDO_REMOVE_LAYOUT_DISMISS
import kotlinx.android.synthetic.main.fragment_income_expense.income_expense_view_pager

class IncomeExpenseFragment :
    BaseFragment<FragmentIncomeExpenseBinding, IncomeExpenseNavigator>(R.layout.fragment_income_expense),
    IncomeExpenseNavigator,
    OnKeyboardVisibleChange {

    private val args by navArgs<IncomeExpenseFragmentArgs>()
    private val transactionType by lazy { args.transactionType }

    override fun performDataBinding(view: View) = FragmentIncomeExpenseBinding.bind(view)
    override fun getNavigator() = this
    override val viewModel by viewModels<IncomeExpenseViewModel>()

    private lateinit var demoCollectionPagerAdapter: IncomeExpenseAdapter
    private var currentPage = 0
    private var isKeyboardShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        demoCollectionPagerAdapter =
            IncomeExpenseAdapter(
                requireContext(),
                childFragmentManager
            )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            ARG_UNDO_REMOVE_LAYOUT_DISMISS
        )?.observe(
            viewLifecycleOwner
        ) {
            if (it) {
                val currentFragment =
                    demoCollectionPagerAdapter.getRegisteredFragment(currentPage) as? OnDialogDismissListener
                currentFragment?.onDismiss()
            }
            findNavController().previousBackStackEntry?.savedStateHandle?.remove<Boolean>(
                ARG_UNDO_REMOVE_LAYOUT_DISMISS
            )
        }
        viewBinding?.let {
            it.incomeExpenseViewPager.addOnPageChangeListener(object :
                    ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) { /* do notjing */
                    }

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) { /* do nothing */
                    }

                    override fun onPageSelected(position: Int) {
                        currentPage = position
                        val currentFragment = getCurrentPagedFragment()
                        if (isKeyboardShown) {
                            currentFragment.onOpenKeyboard()
                        } else {
                            currentFragment.onCloseKeyboard()
                        }
                    }
                })
            it.incomeExpenseTabs.setupWithViewPager(income_expense_view_pager)
            it.incomeExpenseViewPager.adapter = demoCollectionPagerAdapter
            it.incomeExpenseViewPager.currentItem =
                if (transactionType == TransactionType.INCOME) {
                    1
                } else {
                    0
                }
        }
    }

    override fun onStop() {
        isKeyboardShown = false
        super.onStop()
    }

    private fun getCurrentPagedFragment(): OnKeyboardVisibleChange {
        return demoCollectionPagerAdapter.getRegisteredFragment(currentPage) as OnKeyboardVisibleChange
    }

    override fun onOpenKeyboard() {
        val currentFragment = getCurrentPagedFragment()
        currentFragment.onOpenKeyboard()
        isKeyboardShown = true
    }

    override fun onCloseKeyboard() {
        val currentFragment = getCurrentPagedFragment()
        currentFragment.onCloseKeyboard()
        isKeyboardShown = false
    }

    companion object {
        const val ARG_TRANSACTION_CREATED = "transaction_created"
    }
}
