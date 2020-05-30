package com.d9tilov.moneymanager.home.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseActivity
import com.d9tilov.moneymanager.base.ui.navigator.HomeNavigator
import com.d9tilov.moneymanager.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(), HomeNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findNavController(R.id.mainNavGraph).addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.category_dest) {
                viewBinding.bottomNavigation.visibility = View.GONE
            } else {
                viewBinding.bottomNavigation.visibility = View.VISIBLE
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
}
