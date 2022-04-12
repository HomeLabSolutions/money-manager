package com.d9tilov.moneymanager.budget.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.currencyCode
import com.d9tilov.moneymanager.base.ui.navigator.BudgetAmountNavigator
import com.d9tilov.moneymanager.budget.BudgetDestination
import com.d9tilov.moneymanager.budget.vm.BudgetAmountViewModel
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.databinding.FragmentBudgetAmountBinding
import com.d9tilov.moneymanager.prepopulate.ui.ControlsClicked
import com.d9tilov.moneymanager.prepopulate.ui.PrepopulateActivity
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BudgetAmountFragment :
    BaseFragment<BudgetAmountNavigator, FragmentBudgetAmountBinding>(
        FragmentBudgetAmountBinding::inflate,
        R.layout.fragment_budget_amount
    ),
    BudgetAmountNavigator,
    ControlsClicked {

    private val args by navArgs<BudgetAmountFragmentArgs>()
    private val destination by lazy { args.destination }

    private var toolbar: MaterialToolbar? = null

    override fun getNavigator() = this
    override val viewModel by viewModels<BudgetAmountViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewBinding?.run {
            if (destination == BudgetDestination.PROFILE_SCREEN) {
                createdBudgetSave.show()
                createdBudgetSave.isSelected = true
                createdBudgetSave.setOnClickListener {
                    viewModel.saveBudgetAmount(commonBudgetAmount.getValue())
                    findNavController().popBackStack()
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.budgetData
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect {
                        commonBudgetAmount.setValue(it.sum, currencyCode())
                        if (it.sum.signum() == 0) commonBudgetAmount.moneyEditText.setSelection(1)
                        else commonBudgetAmount.moneyEditText.setSelection(commonBudgetAmount.moneyEditText.length())
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (activity is PrepopulateActivity) {
            (activity as PrepopulateActivity).controlsClick = this
        }
        showKeyboard(viewBinding?.commonBudgetAmount?.moneyEditText)
    }

    override fun onStop() {
        super.onStop()
        if (activity is PrepopulateActivity) {
            (activity as PrepopulateActivity).controlsClick = null
        }
    }

    private fun initToolbar() {
        toolbar = viewBinding?.commonBudgetToolbarContainer?.toolbar
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.title_prepopulate_budget)
        if (destination == BudgetDestination.PROFILE_SCREEN) {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun goToRegularIncomeScreen() {}

    override fun showError() {}

    override fun onNextClick() {
        viewBinding?.commonBudgetAmount?.let { viewModel.saveBudgetAmount(it.getValue()) }
    }
}
