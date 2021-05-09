package com.d9tilov.moneymanager.budget.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.BudgetAmountNavigator
import com.d9tilov.moneymanager.budget.vm.BudgetAmountViewModel
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.databinding.FragmentBudgetAmountBinding
import com.d9tilov.moneymanager.prepopulate.ui.ControlsClicked
import com.d9tilov.moneymanager.prepopulate.ui.PrepopulateActivity
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BudgetAmountFragment :
    BaseFragment<BudgetAmountNavigator>(R.layout.fragment_budget_amount),
    BudgetAmountNavigator,
    ControlsClicked {

    private val viewBinding by viewBinding(FragmentBudgetAmountBinding::bind)
    private var toolbar: MaterialToolbar? = null

    override fun getNavigator() = this

    override val viewModel by viewModels<BudgetAmountViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewModel.getAmount().observe(
            this.viewLifecycleOwner,
            {
                viewBinding.commonBudgetAmount.setValue(it.sum)
            }
        )
    }

    override fun onStart() {
        super.onStart()
        (activity as PrepopulateActivity).controlsClick = this
        showKeyboard(viewBinding.commonBudgetAmount.moneyEditText)
    }

    override fun onStop() {
        (activity as PrepopulateActivity).controlsClick = null
        super.onStop()
    }

    private fun initToolbar() {
        toolbar = viewBinding.commonBudgetToolbarContainer.toolbar
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.title_prepopulate_budget)
        activity.setSupportActionBar(toolbar)
    }

    override fun goToRegularIncomeScreen() {
        TODO("Not yet implemented")
    }

    override fun showError() {
        TODO("Not yet implemented")
    }

    override fun onNextClick() {
        viewModel.saveBudgetAmount(viewBinding.commonBudgetAmount.getValue())
    }
}
