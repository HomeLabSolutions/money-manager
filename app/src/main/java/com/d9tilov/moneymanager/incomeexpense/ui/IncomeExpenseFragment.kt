package com.d9tilov.moneymanager.incomeexpense.ui

import android.os.Bundle
import android.view.View
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.IncomeExpenseNavigator
import com.d9tilov.moneymanager.databinding.FragmentIncomeExpenseBinding
import com.d9tilov.moneymanager.incomeexpense.adapter.IncomeExpenseAdapter
import com.d9tilov.moneymanager.incomeexpense.vm.IncomeExpenseViewModel
import kotlinx.android.synthetic.main.fragment_income_expense.*
import javax.inject.Inject

class IncomeExpenseFragment : BaseFragment<FragmentIncomeExpenseBinding, IncomeExpenseViewModel>(),
    IncomeExpenseNavigator {

    @Inject
    internal lateinit var viewModel: IncomeExpenseViewModel
    private lateinit var demoCollectionPagerAdapter: IncomeExpenseAdapter

    override fun performDataBinding(view: View): FragmentIncomeExpenseBinding =
        FragmentIncomeExpenseBinding.bind(view)

    override fun getLayoutId() = R.layout.fragment_income_expense
    override fun getViewModel() = viewModel
    override fun getNavigator() = this

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        demoCollectionPagerAdapter =
            IncomeExpenseAdapter(
                requireContext(),
                childFragmentManager
            )
        viewBinding.incomeExpenseTabs.setupWithViewPager(income_expense_view_pager)
        viewBinding.incomeExpenseViewPager.adapter = demoCollectionPagerAdapter
    }
}
