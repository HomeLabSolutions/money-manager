package com.d9tilov.moneymanager.incomeexpense.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.d9tilov.moneymanager.incomeexpense.R
import com.d9tilov.moneymanager.incomeexpense.presentation.adapter.IncomeExpenseAdapter
import com.d9tilov.moneymanager.incomeexpense.databinding.FragmentIncomeExpenseBinding
import kotlinx.android.synthetic.main.fragment_income_expense.*

class IncomeExpenseFragment : Fragment(R.layout.fragment_income_expense) {

    private lateinit var binding: FragmentIncomeExpenseBinding
    private lateinit var demoCollectionPagerAdapter: IncomeExpenseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIncomeExpenseBinding.bind(view)
        demoCollectionPagerAdapter =
            IncomeExpenseAdapter(
                requireContext(),
                childFragmentManager
            )
        binding.incomeExpenseTabs.setupWithViewPager(income_expense_view_pager)
        binding.incomeExpenseViewPager.adapter = demoCollectionPagerAdapter
    }
}
