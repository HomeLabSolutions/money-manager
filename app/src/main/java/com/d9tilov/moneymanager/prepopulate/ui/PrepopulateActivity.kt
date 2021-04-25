package com.d9tilov.moneymanager.prepopulate.ui

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseActivity
import com.d9tilov.moneymanager.budget.ui.BudgetAmountFragmentDirections
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.currency.ui.CurrencyFragmentDirections
import com.d9tilov.moneymanager.databinding.ActivityPrepopulateBinding
import com.d9tilov.moneymanager.fixed.ui.FixedExpenseFragmentDirections
import com.d9tilov.moneymanager.fixed.ui.FixedIncomeFragmentDirections
import com.d9tilov.moneymanager.home.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrepopulateActivity : BaseActivity<ActivityPrepopulateBinding>() {

    private val fullScreenDestinationSet =
        setOf(
            R.id.fixed_created_transaction_dest,
            R.id.category_dest,
            R.id.edit_category_dialog,
            R.id.remove_sub_category_dialog,
            R.id.remove_category_dialog,
            R.id.unit_category_to_folder_dialog,
            R.id.category_set_dest,
            R.id.category_creation_dest,
            R.id.sub_category_dest,
            R.id.goal_creation_dest
        )
    var controlsClick: ControlsClicked? = null

    override fun performDataBinding() = ActivityPrepopulateBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.prepopulate_nav_host_container) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            setControlsVisibility(!fullScreenDestinationSet.contains(destination.id))
            enableBackButton(destination.id != R.id.choose_currency_dest)
            updateProgress(navController.backStack.size)
        }
        viewBinding.prepopulateNextBtn.setOnClickListener {
            val action: NavDirections
            when (navController.currentDestination?.id) {
                R.id.choose_currency_dest -> {
                    action =
                        CurrencyFragmentDirections.toCommonAmountDest()
                    navController.navigate(action)
                }
                R.id.budget_amount_dest -> {
                    action =
                        BudgetAmountFragmentDirections.toFixedIncomeDest()
                    navController.navigate(action)
                }
                R.id.fixed_income_dest -> {
                    action =
                        FixedIncomeFragmentDirections.toFixedExpenseDest()
                    navController.navigate(action)
                }
                R.id.fixed_expense_dest -> {
                    action = FixedExpenseFragmentDirections.toGoalsDest()
                    navController.navigate(action)
                }
                else -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
            controlsClick?.onNextClick()
        }
        viewBinding.prepopulateBackBtn.setOnClickListener {
            navController.popBackStack()
            controlsClick?.onBackClick()
        }
    }

    private fun setControlsVisibility(visible: Boolean) {
        viewBinding.run {
            if (visible) {
                prepopulateBackBtn.show()
                prepopulateNextBtn.show()
                prepopulateProgress.show()
            } else {
                prepopulateBackBtn.gone()
                prepopulateNextBtn.gone()
                prepopulateProgress.gone()
            }
        }
    }

    private fun enableBackButton(enable: Boolean) {
        viewBinding.prepopulateBackBtn.isEnabled = enable
        val typedValue = TypedValue()
        resources.getValue(R.dimen.alpha_disable, typedValue, true)
        val disableValue = typedValue.float
        viewBinding.prepopulateBackBtn.alpha =
            if (enable) 1f else disableValue
    }

    private fun updateProgress(progress: Int) {
        (supportFragmentManager.findFragmentById(R.id.prepopulate_nav_host_container) as NavHostFragment).childFragmentManager.backStackEntryCount
        viewBinding.prepopulateProgress.setProgress(
            progress - 1,
            MAX_SCREEN_AMOUNT
        )
    }

    companion object {
        private const val MAX_SCREEN_AMOUNT = 5
    }
}

interface ControlsClicked {
    fun onNextClick() {}
    fun onBackClick() {}
}