package com.d9tilov.moneymanager.splash.ui

import com.d9tilov.moneymanager.base.ui.BaseActivity
import com.d9tilov.moneymanager.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun performDataBinding() = ActivitySplashBinding.inflate(layoutInflater)
}
