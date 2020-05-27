package com.d9tilov.moneymanager.incomeexpense.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.incomeexpense.expense.ui.ExpenseFragment
import com.d9tilov.moneymanager.incomeexpense.income.ui.IncomeFragment

class IncomeExpenseAdapter(private val context: Context, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        private const val TAB_COUNT = 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ExpenseFragment()
            }
            else -> {
                return IncomeFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> context.getString(R.string.tab_expense)
            else -> context.getString(R.string.tab_income)
        }
    }

    override fun getCount() =
        TAB_COUNT
}
