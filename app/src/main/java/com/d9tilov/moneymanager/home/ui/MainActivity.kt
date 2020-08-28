package com.d9tilov.moneymanager.home.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseActivity
import com.d9tilov.moneymanager.base.ui.navigator.HomeNavigator
import com.d9tilov.moneymanager.core.events.OnBackPressed
import com.d9tilov.moneymanager.core.events.OnKeyboardVisibleChange
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.core.util.px
import com.d9tilov.moneymanager.core.util.setupWithNavController
import com.d9tilov.moneymanager.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    BaseActivity<ActivityMainBinding>(),
    HomeNavigator,
    DialogInterface.OnDismissListener {

    var forceShowKeyboard = true
    private var currentNavController: LiveData<NavController>? = null
    private lateinit var topView: View
    private var isKeyboardShown = false
    private lateinit var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        topView = window.decorView.findViewById<View>(android.R.id.content)
        globalLayoutListener = KeyboardGlobalLayoutListener()
        topView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
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
        val navGraphIds = listOf(R.navigation.home_navigation, R.navigation.chart_navigation, R.navigation.profile_navigation)
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
                    when (destination.id) {
                        R.id.income_expense_dest,
                        R.id.chart_dest,
                        R.id.settings_dest ->
                            viewBinding.bottomNav.visibility = VISIBLE
                        else -> viewBinding.bottomNav.visibility = GONE
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
        viewBinding.bottomNav.visibility = GONE
    }

    override fun onCloseKeyboard() {
        super.onCloseKeyboard()
        forceShowKeyboard = false
        if (findNavController(R.id.nav_host_container).currentDestination?.id == R.id.income_expense_dest) {
            viewBinding.bottomNav.visibility = VISIBLE
        }
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

    private fun getCurrentFragment(): Fragment? {
        val navHostFragment =
            this.supportFragmentManager.findFragmentById(R.id.nav_host_container)
        var currentFragment: Fragment? = null
        navHostFragment?.let {
            currentFragment = it.childFragmentManager.fragments[0]
        }
        return currentFragment
    }

    override fun onDestroy() {
        topView.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
        super.onDestroy()
    }

    inner class KeyboardGlobalLayoutListener : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val heightDifference = topView.rootView.height - topView.height
            val currentFragment = getCurrentFragment()
            if (heightDifference > 200.px) {
                if (isKeyboardShown) {
                    return
                }
                isKeyboardShown = true
                onOpenKeyboard()
                (currentFragment as? OnKeyboardVisibleChange)?.onOpenKeyboard()
            } else {
                if (!isKeyboardShown) {
                    return
                }
                isKeyboardShown = false
                onCloseKeyboard()
                (currentFragment as? OnKeyboardVisibleChange)?.onCloseKeyboard()
            }
        }
    }
}
