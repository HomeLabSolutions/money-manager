package com.d9tilov.moneymanager.incomeexpense.income.ui

import android.view.View
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.IncomeNavigator
import com.d9tilov.moneymanager.databinding.FragmentIncomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncomeFragment : BaseFragment<FragmentIncomeBinding, IncomeNavigator>(R.layout.fragment_income), IncomeNavigator {

    override fun performDataBinding(view: View): FragmentIncomeBinding =
        FragmentIncomeBinding.bind(view)

    override val viewModel by viewModels<IncomeViewModel>()
    override fun getNavigator() = this

    companion object {
        fun newInstance() = IncomeFragment()
    }
}
