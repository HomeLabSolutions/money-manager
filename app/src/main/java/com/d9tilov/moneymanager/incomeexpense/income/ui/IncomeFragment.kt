package com.d9tilov.moneymanager.incomeexpense.income.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.IncomeNavigator
import com.d9tilov.moneymanager.incomeexpense.income.di.inject
import javax.inject.Inject

class IncomeFragment : Fragment(), IncomeNavigator {

    @Inject
    internal lateinit var viewModel: IncomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        viewModel.navigator = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_income, container, false)
    }

}
