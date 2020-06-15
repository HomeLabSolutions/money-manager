package com.d9tilov.moneymanager.incomeexpense.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.IncomeExpenseNavigator
import com.d9tilov.moneymanager.core.events.OnKeyboardVisibleChange
import com.d9tilov.moneymanager.databinding.FragmentIncomeExpenseBinding
import com.d9tilov.moneymanager.incomeexpense.ui.adapter.IncomeExpenseAdapter
import kotlinx.android.synthetic.main.fragment_income_expense.income_expense_view_pager

class IncomeExpenseFragment :
    Fragment(R.layout.fragment_income_expense),
    IncomeExpenseNavigator,
    OnKeyboardVisibleChange {

    private lateinit var demoCollectionPagerAdapter: IncomeExpenseAdapter
    private lateinit var viewBinding: FragmentIncomeExpenseBinding
    private var currentPage = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentIncomeExpenseBinding.bind(view)
        demoCollectionPagerAdapter =
            IncomeExpenseAdapter(
                requireContext(),
                childFragmentManager
            )
        viewBinding.incomeExpenseViewPager.addOnPageChangeListener(object :
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
                }
            })
        viewBinding.incomeExpenseTabs.setupWithViewPager(income_expense_view_pager)
        viewBinding.incomeExpenseViewPager.adapter = demoCollectionPagerAdapter
    }

    override fun onOpenKeyboard() {
        val currentFragment =
            demoCollectionPagerAdapter.getRegisteredFragment(currentPage) as OnKeyboardVisibleChange
        currentFragment.onOpenKeyboard()
    }

    override fun onCloseKeyboard() {
        val currentFragment =
            demoCollectionPagerAdapter.getRegisteredFragment(currentPage) as OnKeyboardVisibleChange
        currentFragment.onCloseKeyboard()
    }
}
