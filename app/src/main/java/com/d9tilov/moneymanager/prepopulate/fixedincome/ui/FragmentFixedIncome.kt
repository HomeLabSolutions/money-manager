package com.d9tilov.moneymanager.prepopulate.fixedincome.ui

import android.view.View
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.FixedIncomeNavigator
import com.d9tilov.moneymanager.databinding.FragmentFixedIncomeBinding
import com.d9tilov.moneymanager.prepopulate.fixedincome.vm.FixedIncomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentFixedIncome :
    BaseFragment<FragmentFixedIncomeBinding, FixedIncomeNavigator>(R.layout.fragment_fixed_income),
    FixedIncomeNavigator {
    override fun performDataBinding(view: View) = FragmentFixedIncomeBinding.bind(view)

    override fun getNavigator() = this

    override val viewModel by viewModels<FixedIncomeViewModel>()
}
