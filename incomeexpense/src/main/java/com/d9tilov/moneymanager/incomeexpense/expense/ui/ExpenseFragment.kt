package com.d9tilov.moneymanager.incomeexpense.expense.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.incomeexpense.R
import com.d9tilov.moneymanager.incomeexpense.expense.di.inject
import javax.inject.Inject

class ExpenseFragment : Fragment(), ExpenseNavigator {

    @Inject
    internal lateinit var viewModel: ExpenseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        viewModel.setNavigator(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_expense, container, false)
    }
}