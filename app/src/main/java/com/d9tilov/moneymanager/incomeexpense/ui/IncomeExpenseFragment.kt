package com.d9tilov.moneymanager.incomeexpense.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.IncomeExpenseNavigator
import com.d9tilov.moneymanager.databinding.FragmentIncomeExpenseBinding
import com.d9tilov.moneymanager.incomeexpense.ui.adapter.IncomeExpenseAdapter
import kotlinx.android.synthetic.main.fragment_income_expense.*

class IncomeExpenseFragment : Fragment(R.layout.fragment_income_expense),
    IncomeExpenseNavigator {

    private lateinit var demoCollectionPagerAdapter: IncomeExpenseAdapter
    private lateinit var viewBinding: FragmentIncomeExpenseBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentIncomeExpenseBinding.bind(view)
        demoCollectionPagerAdapter =
            IncomeExpenseAdapter(
                requireContext(),
                childFragmentManager
            )
        viewBinding.incomeExpenseTabs.setupWithViewPager(income_expense_view_pager)
        viewBinding.incomeExpenseViewPager.adapter = demoCollectionPagerAdapter
    }
}
