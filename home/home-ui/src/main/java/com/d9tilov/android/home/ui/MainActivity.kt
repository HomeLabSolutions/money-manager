package com.d9tilov.android.home.ui

import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.d9tilov.android.backup.data.impl.PeriodicBackupWorker
import com.d9tilov.android.common_android.ui.base.BaseActivity
import com.d9tilov.android.common_android.utils.gone
import com.d9tilov.android.common_android.utils.hideKeyboard
import com.d9tilov.android.common_android.utils.setupWithNavController
import com.d9tilov.android.common_android.utils.show
import com.d9tilov.android.core.events.OnBackPressed
import com.d9tilov.android.home.ui.navigation.HomeNavigator
import com.d9tilov.android.home_ui.R
import com.d9tilov.android.home_ui.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity :
    BaseActivity<ActivityMainBinding>(),
    HomeNavigator,
    DialogInterface.OnDismissListener {

    private var currentNavController: LiveData<NavController>? = null
    private val setOfShownBottomBar = setOf(
        com.d9tilov.android.incomeexpense_ui.R.id.income_expense_dest,
        com.d9tilov.android.statistics_ui.R.id.chart_dest,
        com.d9tilov.android.profile_ui.R.id.profile_dest,
        com.d9tilov.android.transaction_ui.R.id.remove_transaction_dialog,
    )
    private val viewModel by viewModels<MainViewModel>()
    override val navHostFragmentId = R.id.nav_host_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
        lifecycleScope.launch {
            viewModel.isPremium
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { isPremium ->
                    if (isPremium) PeriodicBackupWorker.startPeriodicJob(this@MainActivity)
                }
        }
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
            com.d9tilov.android.statistics_ui.R.navigation.statistics_navigation,
            com.d9tilov.android.profile_ui.R.navigation.profile_navigation,
        )
        val controller = viewBinding?.bottomNav?.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent,
        )
        // Whenever the selected controller changes, setup the action bar.
        controller?.observe(
            this,
        ) { navController ->
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (setOfShownBottomBar.contains(destination.id)) {
                    showBottomBarWithAnimation()
                } else {
                    viewBinding?.bottomNav?.gone()
                }
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
                    ALPHA_BAR_MAX,
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
