package com.d9tilov.moneymanager.splash.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.backup.PeriodicBackupWorker
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.SplashNavigator
import com.d9tilov.moneymanager.home.ui.MainActivity
import com.d9tilov.moneymanager.prepopulate.ui.PrepopulateActivity
import com.d9tilov.moneymanager.splash.vm.SplashViewModel
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<SplashNavigator>(R.layout.fragment_splash), SplashNavigator {

    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.FacebookBuilder().build()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PeriodicBackupWorker.startPeriodicJob(requireContext())
    }

    override fun getNavigator() = this
    override val viewModel by viewModels<SplashViewModel>()

    override fun openHomeScreen() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

    override fun openPrepopulate() {
        startActivity(Intent(requireContext(), PrepopulateActivity::class.java))
        requireActivity().finish()
    }

    override fun openAuthScreen() {
        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                viewModel.createUser()
                PeriodicBackupWorker.startPeriodicJob(requireContext())
            }
        startForResult.launch(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.ic_money_manager_logo_white)
                .setTheme(R.style.Theme_MoneyManager)
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build()
        )
    }
}
