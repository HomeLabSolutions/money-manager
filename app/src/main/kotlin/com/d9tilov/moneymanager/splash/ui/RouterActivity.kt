package com.d9tilov.moneymanager.splash.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.SplashNavigator
import com.d9tilov.moneymanager.home.ui.MainActivity
import com.d9tilov.moneymanager.splash.vm.RouterViewModel
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RouterActivity : AppCompatActivity(), SplashNavigator {

    private val providers = arrayListOf(
        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    private val viewModel by viewModels<RouterViewModel>()
    private val startForResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { viewModel.updateUid() }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel.navigator = this
        splashScreen.setKeepOnScreenCondition { true }
    }

    override fun openHomeScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }

    override fun openPrepopulate() {
        val intent = Intent()
        intent.setClassName(this, "com.d9tilov.android.prepopulate.ui.PrepopulateActivity")
        startActivity(intent)
        this.finish()
    }

    override fun openAuthScreen() {
        startForResult.launch(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.ic_money_manager_logo)
                .setTheme(R.style.Theme_Login)
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build()
        )
    }
}
