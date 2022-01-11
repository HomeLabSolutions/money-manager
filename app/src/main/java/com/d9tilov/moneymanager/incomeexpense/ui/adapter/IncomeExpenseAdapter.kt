package com.d9tilov.moneymanager.incomeexpense.ui.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.d9tilov.moneymanager.incomeexpense.expense.ui.ExpenseFragment
import com.d9tilov.moneymanager.incomeexpense.income.ui.IncomeFragment

class IncomeExpenseAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val registeredFragments = SparseArray<Fragment>()

    fun getRegisteredFragment(position: Int): Fragment {
        return registeredFragments[position]
    }

    override fun getItemCount(): Int = TAB_COUNT
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = ExpenseFragment.newInstance()
                registeredFragments.put(position, fragment)
                fragment
            }
            else -> {
                val fragment = IncomeFragment.newInstance()
                registeredFragments.put(position, fragment)
                fragment
            }
        }
    }

    fun isAdapterReady() = registeredFragments.size() == TAB_COUNT

    companion object {
        private const val TAB_COUNT = 2
    }
}
