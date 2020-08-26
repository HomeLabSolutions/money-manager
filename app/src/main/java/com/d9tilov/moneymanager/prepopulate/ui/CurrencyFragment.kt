package com.d9tilov.moneymanager.prepopulate.ui

import android.view.View
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.CurrencyNavigator
import com.d9tilov.moneymanager.databinding.FragmentCurrencyBinding
import com.d9tilov.moneymanager.prepopulate.vm.CurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyFragment :
    BaseFragment<FragmentCurrencyBinding, CurrencyNavigator>(R.layout.fragment_currency),
    CurrencyNavigator {

    override fun performDataBinding(view: View) = FragmentCurrencyBinding.bind(view)
    override fun getNavigator(): CurrencyNavigator = this
    override val viewModel by viewModels<CurrencyViewModel>()

    override fun goToCommonAmountScreen() {
        TODO("Not yet implemented")
    }

    override fun save() {
        TODO("Not yet implemented")
    }

    override fun skip() {
        TODO("Not yet implemented")
    }
}
