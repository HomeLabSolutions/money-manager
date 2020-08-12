package com.d9tilov.moneymanager.home.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseActivity
import com.d9tilov.moneymanager.base.ui.navigator.HomeNavigator
import com.d9tilov.moneymanager.databinding.ActivityMainBinding
import com.mfms.common.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    BaseActivity<ActivityMainBinding>(),
    HomeNavigator,
    DialogInterface.OnDismissListener {

    var forceShowKeyboard = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findNavController(R.id.mainNavGraph).addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.income_expense_dest,
                R.id.chart_dest,
                R.id.settings_dest ->
                    viewBinding.bottomNavigation.visibility = VISIBLE
                else -> viewBinding.bottomNavigation.visibility = GONE
            }
        }
        viewBinding.bottomNavigation.setupWithNavController(
            Navigation.findNavController(
                this,
                R.id.mainNavGraph
            )
        )
    }

    override fun performDataBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onOpenKeyboard() {
        super.onOpenKeyboard()
        viewBinding.bottomNavigation.visibility = GONE
    }

    override fun onCloseKeyboard() {
        super.onCloseKeyboard()
        forceShowKeyboard = false
        if (findNavController(R.id.mainNavGraph).currentDestination?.id == R.id.income_expense_dest) {
            viewBinding.bottomNavigation.visibility = VISIBLE
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        hideKeyboard()
    }
}
