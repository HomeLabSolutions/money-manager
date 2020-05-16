package com.d9tilov.moneymanager.home

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseActivity
import com.d9tilov.moneymanager.databinding.ActivityMainBinding
import com.d9tilov.moneymanager.home.di.inject

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.setupWithNavController(
            Navigation.findNavController(
                this,
                R.id.my_nav_host_fragment
            )
        )
    }
}
