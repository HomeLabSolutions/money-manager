package com.d9tilov.moneymanager.splash.ui

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.SplashNavigator
import com.d9tilov.moneymanager.databinding.FragmentSplashBinding
import com.d9tilov.moneymanager.home.ui.MainActivity
import com.d9tilov.moneymanager.prepopulate.ui.PrepopulateActivity
import com.d9tilov.moneymanager.splash.vm.SplashViewModel
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment :
    BaseFragment<SplashNavigator, FragmentSplashBinding>(
        FragmentSplashBinding::inflate,
        R.layout.fragment_splash
    ),
    SplashNavigator {

    private val providers = arrayListOf(
        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    override fun getNavigator() = this
    override val viewModel by viewModels<SplashViewModel>()
    private val startForResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { viewModel.updateData() }

    override fun openHomeScreen() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

    override fun openPrepopulate() {
        startActivity(Intent(requireContext(), PrepopulateActivity::class.java))
        requireActivity().finish()
    }

    override fun openAuthScreen() {
        startForResult.launch(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.ic_money_manager_logo_on_login)
                .setTheme(R.style.Theme_Login)
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build()
        )
    }
}
