package com.d9tilov.moneymanager.home.ui

import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseActivity
import com.d9tilov.moneymanager.base.ui.navigator.HomeNavigator
import com.d9tilov.moneymanager.core.events.OnBackPressed
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.core.util.setupWithNavController
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    BaseActivity<ActivityMainBinding>(),
    HomeNavigator,
    DialogInterface.OnDismissListener {

    private var currentNavController: LiveData<NavController>? = null
    private val setOfShownBottomBar = setOf(
        R.id.income_expense_dest,
        R.id.chart_dest,
        R.id.profile_dest,
        R.id.logout_dialog,
        R.id.remove_transaction_dialog,
    )

    override val navHostFragmentId = R.id.nav_host_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.home_navigation,
            R.navigation.statistics_navigation,
            R.navigation.profile_navigation
        )
        val controller = viewBinding?.bottomNav?.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )
        // Whenever the selected controller changes, setup the action bar.
        controller?.observe(
            this
        ) { navController ->
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (setOfShownBottomBar.contains(destination.id)) showBottomBarWithAnimation()
                else viewBinding?.bottomNav?.gone()
            }
        }
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    override fun performDataBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        hideKeyboard()
    }

    override fun onBackPressed() {
        val currentFragment = getCurrentFragment()
        val interceptBack = (currentFragment as? OnBackPressed)?.onBackPressed() ?: true
        if (!interceptBack) return
        val backStackCount =
            this.supportFragmentManager.findFragmentById(R.id.nav_host_container)?.childFragmentManager?.backStackEntryCount
                ?: 0
        if (backStackCount > 0) findNavController(R.id.nav_host_container).popBackStack() else super.onBackPressed()
    }

    private fun showBottomBarWithAnimation() {
        viewBinding?.run {
            if (bottomNav.isVisible) return
            val bottomBarAnimationAppear =
                ObjectAnimator.ofFloat(
                    bottomNav,
                    View.ALPHA,
                    ALPHA_BAR_MIN,
                    ALPHA_BAR_MAX
                ).apply {
                    duration = ANIMATION_DURATION_BAR
                    interpolator = AccelerateInterpolator()
                }
            bottomNav.show()
            bottomBarAnimationAppear.start()
        }
    }

    companion object {
        private const val ALPHA_BAR_MIN = 0f
        private const val ALPHA_BAR_MAX = 1f
        private const val ANIMATION_DURATION_BAR = 500L
    }
}
