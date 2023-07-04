package com.d9tilov.android.splash.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.splash.ui.navigation.SplashNavigator
import com.d9tilov.android.splash.ui.vm.RouterViewModel
import com.d9tilov.android.splash_ui.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RouterActivity : AppCompatActivity(), SplashNavigator {

    private val providers = arrayListOf(
        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    private val viewModel by viewModels<RouterViewModel>()
    private val startForResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
            Timber.tag(TAG).d("Sign in result: $result")
            viewModel.updateData()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel.navigator = this
        splashScreen.setKeepOnScreenCondition { true }
    }

    override fun openHomeScreen() {
        val intent = Intent()
        intent.setClassName(this, "com.d9tilov.moneymanager.home.ui.MainActivity")
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
