package com.d9tilov.moneymanager.prepopulate.ui

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseActivity
import com.d9tilov.moneymanager.databinding.ActivityPrepopulateBinding
import com.d9tilov.moneymanager.home.ui.MainActivity
import com.d9tilov.moneymanager.prepopulate.amount.ui.CommonAmountFragmentDirections
import com.d9tilov.moneymanager.prepopulate.currency.ui.CurrencyFragmentDirections
import com.d9tilov.moneymanager.prepopulate.fixedexpense.ui.FixedExpenseFragmentDirections
import com.d9tilov.moneymanager.prepopulate.fixedincome.ui.FragmentFixedIncomeDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrepopulateActivity : BaseActivity<ActivityPrepopulateBinding>() {

    private var backStackEntryCount = 1

    override fun performDataBinding() = ActivityPrepopulateBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.prepopulate_nav_host_container) as NavHostFragment
        val navController = navHostFragment.navController
        viewBinding.prepopulateProgress.setProgress(
            backStackEntryCount,
            MAX_SCREEN_AMOUNT
        )
        viewBinding.prepopulateNextBtn.setOnClickListener {
            val action: NavDirections
            backStackEntryCount += 1
            when (navController.currentDestination?.id) {
                R.id.choose_currency_dest ->
                    action =
                        CurrencyFragmentDirections.toCommonAmountDest()
                R.id.common_amount_dest ->
                    action =
                        CommonAmountFragmentDirections.toFixedIncomeDest()
                R.id.fixed_income_dest ->
                    action =
                        FragmentFixedIncomeDirections.toFixedExpenseDest()
                R.id.fixed_expense_dest -> action = FixedExpenseFragmentDirections.toGoalsDest()
                else -> {
                    action = FixedExpenseFragmentDirections.toGoalsDest()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
            navController.navigate(action)
        }
        viewBinding.prepopulateBackBtn.setOnClickListener {
            backStackEntryCount -= 1
            navController.popBackStack()
        }
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            enableBackButton(destination.id != R.id.choose_currency_dest)
            updateProgress()
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

    private fun updateProgress() {
        (supportFragmentManager.findFragmentById(R.id.prepopulate_nav_host_container) as NavHostFragment).childFragmentManager.backStackEntryCount
        viewBinding.prepopulateProgress.setProgress(
            backStackEntryCount,
            MAX_SCREEN_AMOUNT
        )
    }

    companion object {
        private const val MAX_SCREEN_AMOUNT = 5
    }
}
