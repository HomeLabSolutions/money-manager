package com.d9tilov.moneymanager.home.ui

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseActivity
import com.d9tilov.moneymanager.base.ui.navigator.HomeNavigator
import com.d9tilov.moneymanager.databinding.ActivityMainBinding
import com.d9tilov.moneymanager.home.ui.vm.HomeViewModel

class HomeActivity : BaseActivity<ActivityMainBinding, HomeViewModel>(), HomeNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding.bottomNavigation.setupWithNavController(
            Navigation.findNavController(
                this,
                R.id.my_nav_host_fragment
            )
        )
    }

    override fun performDataBinding(): ActivityMainBinding {
        initObservers()
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun getNavigator() = this
    override fun getViewModelClass() = HomeViewModel::class.java
}
