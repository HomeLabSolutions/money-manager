package com.d9tilov.moneymanager.incomeexpense.ui.adapter

import android.content.Context
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.incomeexpense.expense.ui.ExpenseFragment
import com.d9tilov.moneymanager.incomeexpense.income.ui.IncomeFragment

class IncomeExpenseAdapter(private val context: Context, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val registeredFragments = SparseArray<Fragment>()

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ExpenseFragment.newInstance()
            }
            else -> {
                return IncomeFragment.newInstance()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> context.getString(R.string.tab_expense)
            else -> context.getString(R.string.tab_income)
        }
    }

    fun getRegisteredFragment(position: Int): Fragment {
        return registeredFragments[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        registeredFragments.put(position, fragment)
        return fragment
    }

    override fun getCount() = TAB_COUNT

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, obj)
    }

    companion object {
        private const val TAB_COUNT = 2
    }
}
