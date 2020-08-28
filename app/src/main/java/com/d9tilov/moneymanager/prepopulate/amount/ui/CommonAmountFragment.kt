package com.d9tilov.moneymanager.prepopulate.amount.ui

import android.view.View
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.CommonAmountNavigator
import com.d9tilov.moneymanager.databinding.FragmentCommonAmountBinding
import com.d9tilov.moneymanager.prepopulate.amount.vm.CommonAmountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommonAmountFragment :
    BaseFragment<FragmentCommonAmountBinding, CommonAmountNavigator>(R.layout.fragment_common_amount),
    CommonAmountNavigator {

    override fun performDataBinding(view: View) = FragmentCommonAmountBinding.bind(view)

    override fun getNavigator() = this

    override val viewModel by viewModels<CommonAmountViewModel>()

    override fun goToFixedIncomeScreen() {
        TODO("Not yet implemented")
    }

    override fun showError() {
        TODO("Not yet implemented")
    }
}
