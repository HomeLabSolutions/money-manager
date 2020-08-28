package com.d9tilov.moneymanager.prepopulate.fixedexpense.ui

import android.view.View
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.FixedExpenseNavigator
import com.d9tilov.moneymanager.databinding.FragmentFixedExpenseBinding
import com.d9tilov.moneymanager.prepopulate.fixedexpense.vm.FixedExpenseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FixedExpenseFragment :
    BaseFragment<FragmentFixedExpenseBinding, FixedExpenseNavigator>(R.layout.fragment_fixed_expense),
    FixedExpenseNavigator {

    override fun performDataBinding(view: View) = FragmentFixedExpenseBinding.bind(view)

    override fun getNavigator() = this

    override val viewModel by viewModels<FixedExpenseViewModel>()
}
