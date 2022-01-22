package com.d9tilov.moneymanager.budget.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.BudgetAmountNavigator
import com.d9tilov.moneymanager.budget.BudgetDestination
import com.d9tilov.moneymanager.budget.vm.BudgetAmountViewModel
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.databinding.FragmentBudgetAmountBinding
import com.d9tilov.moneymanager.prepopulate.ui.ControlsClicked
import com.d9tilov.moneymanager.prepopulate.ui.PrepopulateActivity
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal

@AndroidEntryPoint
class BudgetAmountFragment :
    BaseFragment<BudgetAmountNavigator>(R.layout.fragment_budget_amount),
    BudgetAmountNavigator,
    ControlsClicked {

    private val args by navArgs<BudgetAmountFragmentArgs>()
    private val destination by lazy { args.destination }

    private val viewBinding by viewBinding(FragmentBudgetAmountBinding::bind)
    private var toolbar: MaterialToolbar? = null

    override fun getNavigator() = this

    override val viewModel by viewModels<BudgetAmountViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        if (destination == BudgetDestination.PROFILE_SCREEN) {
            viewBinding.createdBudgetSave.run {
                show()
                isSelected = true
                setOnClickListener {
                    viewModel.saveBudgetAmount(viewBinding.commonBudgetAmount.getValue())
                    findNavController().popBackStack()
                }
            }
        }
        viewModel.budgetData.observe(
            this.viewLifecycleOwner
        ) {
            viewBinding.commonBudgetAmount.setValue(it.sum)
            if (it.sum.compareTo(BigDecimal.ZERO) == 0) {
                viewBinding.commonBudgetAmount.moneyEditText.setSelection(1)
            } else {
                viewBinding.commonBudgetAmount.moneyEditText.setSelection(viewBinding.commonBudgetAmount.moneyEditText.length())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (activity is PrepopulateActivity) {
            (activity as PrepopulateActivity).controlsClick = this
        }
        showKeyboard(viewBinding.commonBudgetAmount.moneyEditText)
    }

    override fun onStop() {
        super.onStop()
        if (activity is PrepopulateActivity) {
            (activity as PrepopulateActivity).controlsClick = null
        }
    }

    private fun initToolbar() {
        toolbar = viewBinding.commonBudgetToolbarContainer.toolbar
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.title_prepopulate_budget)
        activity.setSupportActionBar(toolbar)
        if (destination == BudgetDestination.PROFILE_SCREEN) {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
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
