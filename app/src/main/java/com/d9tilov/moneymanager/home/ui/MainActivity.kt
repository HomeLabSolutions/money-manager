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
import com.d9tilov.moneymanager.backup.PeriodicBackupWorker
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
    private val bottomMenuSet =
        setOf(R.id.income_expense_dest, R.id.chart_dest, R.id.profile_dest)
    private val setOfShownBottomBar = setOf(
        R.id.income_expense_dest,
        R.id.chart_dest,
        R.id.profile_dest,
        R.id.logout_dialog,
        R.id.remove_transaction_dialog
    )

    override val navHostFragmentId = R.id.nav_host_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
        PeriodicBackupWorker.startPeriodicJob(this)
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
            R.navigation.chart_navigation,
            R.navigation.profile_navigation
        )
        val controller = viewBinding.bottomNav.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )
        // Whenever the selected controller changes, setup the action bar.
        controller.observe(
            this,
            { navController ->
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    if (setOfShownBottomBar.contains(destination.id)) {
                        if (!isKeyboardShown) {
                            showBottomBarWithAnimation()
                        }
                    } else {
                        viewBinding.bottomNav.gone()
                    }
                }
            }
        )
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    override fun performDataBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onOpenKeyboard() {
        super.onOpenKeyboard()
        viewBinding.bottomNav.gone()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        hideKeyboard()
    }

    override fun onBackPressed() {
        val currentFragment = getCurrentFragment()
        val backStackCount =
            this.supportFragmentManager.findFragmentById(R.id.nav_host_container)?.childFragmentManager?.backStackEntryCount
                ?: 0
        (currentFragment as? OnBackPressed)?.onBackPressed()
            ?: if (backStackCount > 0) findNavController(R.id.nav_host_container).popBackStack() else super.onBackPressed()
    }

    override fun onCloseKeyboard() {
        super.onCloseKeyboard()
        val curDest = findNavController(R.id.nav_host_container).currentDestination?.id
        if (bottomMenuSet.contains(curDest)) {
            showBottomBarWithAnimation()
        }
    }

    private fun showBottomBarWithAnimation() {
        if (viewBinding.bottomNav.isVisible) {
            return
        }
        val bottomBarAnimationAppear =
            ObjectAnimator.ofFloat(
                viewBinding.bottomNav,
                View.ALPHA,
                ALPHA_BAR_MIN,
                ALPHA_BAR_MAX
            ).apply {
                duration = ANIMATION_DURATION_BAR
                interpolator = AccelerateInterpolator()
            }
        viewBinding.bottomNav.show()
        bottomBarAnimationAppear.start()
    }

    companion object {
        private const val ALPHA_BAR_MIN = 0f
        private const val ALPHA_BAR_MAX = 1f
        private const val ANIMATION_DURATION_BAR = 500L
    }
}
