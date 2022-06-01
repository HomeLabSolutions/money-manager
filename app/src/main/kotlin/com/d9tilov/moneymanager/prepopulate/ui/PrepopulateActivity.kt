package com.d9tilov.moneymanager.prepopulate.ui

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseActivity
import com.d9tilov.moneymanager.budget.domain.entity.BudgetDestination
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.currency.ui.CurrencyFragmentDirections
import com.d9tilov.moneymanager.databinding.ActivityPrepopulateBinding
import com.d9tilov.moneymanager.home.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrepopulateActivity : BaseActivity<ActivityPrepopulateBinding>() {

    private val fullScreenDestinationSet =
        setOf(
            R.id.category_dest,
            R.id.sub_category_dest,
            R.id.category_set_dest,
            R.id.category_creation_dest,
            R.id.edit_category_dialog,
            R.id.remove_sub_category_dialog,
            R.id.remove_category_dialog,
            R.id.unit_category_to_folder_dialog
        )
    var controlsClick: ControlsClicked? = null

    override fun performDataBinding() = ActivityPrepopulateBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.prepopulate_nav_host_container) as NavHostFragment
        val navController = navHostFragment.navController
        updateProgress(0)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            setControlsVisibility(!fullScreenDestinationSet.contains(destination.id))
            enableBackButton(destination.id != R.id.choose_currency_dest)
        }
        navHostFragment.childFragmentManager.addOnBackStackChangedListener {
            updateProgress(navHostFragment.childFragmentManager.backStackEntryCount)
        }
        viewBinding?.run {
            prepopulateNextBtn.setOnClickListener {
                val action: NavDirections
                when (navController.currentDestination?.id) {
                    R.id.choose_currency_dest -> {
                        action =
                            CurrencyFragmentDirections.toCommonAmountDest(BudgetDestination.PrepopulateScreen)
                        navController.navigate(action)
                    }
                    else -> {
                        startActivity(Intent(this@PrepopulateActivity, MainActivity::class.java))
                        finish()
                    }
                }
                controlsClick?.onNextClick()
            }
            prepopulateBackBtn.setOnClickListener {
                navController.popBackStack()
                controlsClick?.onBackClick()
            }
        }
    }

    private fun setControlsVisibility(visible: Boolean) {
        viewBinding?.run {
            if (visible) {
                prepopulateBackBtn.show()
                prepopulateNextBtn.show()
                prepopulateProgress.show()
                prepopulateProgressTitle.show()
            } else {
                prepopulateBackBtn.gone()
                prepopulateNextBtn.gone()
                prepopulateProgress.gone()
                prepopulateProgressTitle.gone()
            }
        }
    }

    private fun enableBackButton(enable: Boolean) {
        viewBinding?.run {
            prepopulateBackBtn.isEnabled = enable
            val typedValue = TypedValue()
            resources.getValue(R.dimen.alpha_disable, typedValue, true)
            val disableValue = typedValue.float
            prepopulateBackBtn.alpha = if (enable) 1f else disableValue
        }
    }

    private fun updateProgress(progress: Int) {
        val newProgress = progress + 1f
        viewBinding?.run {
            prepopulateProgressTitle.text = getString(
                R.string.prepopulate_progress,
                newProgress.toInt().toString(),
                MAX_SCREEN_AMOUNT.toInt().toString()
            )
            prepopulateProgress.setProgress(newProgress, MAX_SCREEN_AMOUNT)
        }
    }

    companion object {
        private const val MAX_SCREEN_AMOUNT = 2f
    }

    override val navHostFragmentId = R.id.prepopulate_nav_host_container
}

interface ControlsClicked {
    fun onNextClick() {}
    fun onBackClick() {}
}
