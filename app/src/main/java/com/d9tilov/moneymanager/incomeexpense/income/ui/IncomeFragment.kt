package com.d9tilov.moneymanager.incomeexpense.income.ui

import android.view.View
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.IncomeNavigator
import com.d9tilov.moneymanager.databinding.FragmentIncomeBinding
import javax.inject.Inject

class IncomeFragment : BaseFragment<FragmentIncomeBinding, IncomeViewModel>(), IncomeNavigator {

    @Inject
    internal lateinit var viewModel: IncomeViewModel

    override fun performDataBinding(view: View): FragmentIncomeBinding =
        FragmentIncomeBinding.bind(view)

    override fun getLayoutId() = R.layout.fragment_income
    override fun getViewModel() = viewModel
    override fun getNavigator() = this
}
