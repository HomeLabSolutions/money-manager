package com.d9tilov.moneymanager.incomeexpense.income.ui

import android.view.View
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.IncomeNavigator
import com.d9tilov.moneymanager.core.events.OnKeyboardVisibleChange
import com.d9tilov.moneymanager.databinding.FragmentIncomeBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class IncomeFragment :
    BaseFragment<FragmentIncomeBinding, IncomeNavigator>(R.layout.fragment_income),
    IncomeNavigator,
    OnKeyboardVisibleChange {

    override fun performDataBinding(view: View): FragmentIncomeBinding =
        FragmentIncomeBinding.bind(view)

    override val viewModel by viewModels<IncomeViewModel>()
    override fun getNavigator() = this

    companion object {
        fun newInstance() = IncomeFragment()
    }

    override fun onOpenKeyboard() {
        Timber.tag(App.TAG).d("Keyboard shown")
    }

    override fun onCloseKeyboard() {
        Timber.tag(App.TAG).d("Kyboard hidden")
    }
}
