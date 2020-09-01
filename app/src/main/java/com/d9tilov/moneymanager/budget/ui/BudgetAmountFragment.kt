package com.d9tilov.moneymanager.budget.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.BudgetAmountNavigator
import com.d9tilov.moneymanager.budget.vm.BudgetAmountViewModel
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.databinding.FragmentBudgetAmountBinding
import com.d9tilov.moneymanager.prepopulate.ui.ControlsClicked
import com.d9tilov.moneymanager.prepopulate.ui.PrepopulateActivity
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal

@AndroidEntryPoint
class BudgetAmountFragment :
    BaseFragment<FragmentBudgetAmountBinding, BudgetAmountNavigator>(R.layout.fragment_budget_amount),
    BudgetAmountNavigator,
    ControlsClicked {

    private var toolbar: MaterialToolbar? = null

    override fun performDataBinding(view: View) = FragmentBudgetAmountBinding.bind(view)

    override fun getNavigator() = this

    override val viewModel by viewModels<BudgetAmountViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as PrepopulateActivity).controlsClick = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewModel.amount.observe(
            this.viewLifecycleOwner,
            {
                viewBinding?.commonBudgetAmount?.setValue(it)
            }
        )
    }

    override fun onStart() {
        super.onStart()
        showKeyboard(viewBinding?.commonBudgetAmount?.moneyEditText)
    }

    private fun initToolbar() {
        toolbar = viewBinding?.commonBudgetToolbarContainer?.toolbar
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.title_prepopulate_budget)
        activity.setSupportActionBar(toolbar)
    }

    override fun goToFixedIncomeScreen() {
        TODO("Not yet implemented")
    }

    override fun showError() {
        TODO("Not yet implemented")
    }

    override fun onNextClick() {
        viewModel.saveBudgetAmount(viewBinding?.commonBudgetAmount?.getValue() ?: BigDecimal.ZERO)
    }
}
