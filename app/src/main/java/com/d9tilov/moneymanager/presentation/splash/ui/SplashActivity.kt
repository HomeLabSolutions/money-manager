package com.d9tilov.moneymanager.presentation.splash.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseActivity
import com.d9tilov.moneymanager.base.ui.navigator.SplashNavigator
import com.d9tilov.moneymanager.presentation.home.HomeActivity
import com.d9tilov.moneymanager.presentation.splash.di.inject
import com.d9tilov.moneymanager.presentation.splash.vm.SplashViewModel
import com.firebase.ui.auth.AuthUI
import javax.inject.Inject


class SplashActivity : BaseActivity(), SplashNavigator {

    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.FacebookBuilder().build(),
        AuthUI.IdpConfig.TwitterBuilder().build()
    )

    @Inject
    internal lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        viewModel.navigator = this
    }

    override fun openHomeScreen() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    override fun openAuthScreen() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.ic_logo_black)
                .setTheme(R.style.Theme_MoneyManager)
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            viewModel.createUser()
        }
    }

    companion object {

        private const val RC_SIGN_IN = 123
    }
}
