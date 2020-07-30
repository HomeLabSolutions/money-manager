package com.d9tilov.moneymanager.home.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseActivity
import com.d9tilov.moneymanager.base.ui.navigator.HomeNavigator
import com.d9tilov.moneymanager.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), HomeNavigator {

    var forceShowKeyboard = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findNavController(R.id.mainNavGraph).addOnDestinationChangedListener { _, destination, _ ->
            Timber.tag(App.TAG).d("onCreate: destination.id ${destination.id}")
            Timber.tag(App.TAG).d("onCreate: category_dest.id ${R.id.category_dest}")
            Timber.tag(App.TAG).d("onCreate: sub_category_dest.id ${R.id.sub_category_dest}")
            Timber.tag(App.TAG)
                .d("onCreate: edit_transaction_dest.id ${R.id.edit_transaction_dest}")
            when (destination.id) {
                R.id.category_dest,
                R.id.sub_category_dest,
                R.id.edit_transaction_dest ->
                    viewBinding.bottomNavigation.visibility = GONE
                else -> viewBinding.bottomNavigation.visibility = VISIBLE
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
}
