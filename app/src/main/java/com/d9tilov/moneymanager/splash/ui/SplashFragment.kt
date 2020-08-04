package com.d9tilov.moneymanager.splash.ui

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.SplashNavigator
import com.d9tilov.moneymanager.databinding.FragmentSplashBinding
import com.d9tilov.moneymanager.home.ui.MainActivity
import com.d9tilov.moneymanager.splash.vm.SplashViewModel
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashNavigator>(R.layout.fragment_splash), SplashNavigator {

    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.FacebookBuilder().build(),
        AuthUI.IdpConfig.TwitterBuilder().build()
    )

    override fun performDataBinding(view: View) = FragmentSplashBinding.bind(view)
    override fun getNavigator() = this
    override val viewModel by viewModels<SplashViewModel>()

    override fun openHomeScreen() {
        requireActivity().finish()
        startActivity(Intent(requireActivity(), MainActivity::class.java))
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