package com.d9tilov.android.incomeexpense.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.d9tilov.android.incomeexpense.expense.ui.ExpenseFragment
import com.d9tilov.android.incomeexpense.income.ui.IncomeFragment

class IncomeExpenseAdapter(fa: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fa, lifecycle) {

    override fun getItemId(position: Int): Long =
        when (position) {
            0 -> ExpenseFragment.FRAGMENT_ID
            else -> IncomeFragment.FRAGMENT_ID
        }

    override fun getItemCount(): Int = TAB_COUNT
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ExpenseFragment.newInstance()
            else -> IncomeFragment.newInstance()
        }
    }

    companion object {
        const val TAB_COUNT = 2
    }
}
