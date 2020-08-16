package com.d9tilov.moneymanager.settings.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.BuildConfig
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.databinding.FragmentSettingsBinding
import com.d9tilov.moneymanager.settings.ui.vm.SettingsViewModel
import com.d9tilov.moneymanager.splash.ui.SplashActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment :
    BaseFragment<FragmentSettingsBinding, SettingsNavigator>(R.layout.fragment_settings),
    SettingsNavigator {

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun performDataBinding(view: View): FragmentSettingsBinding =
        FragmentSettingsBinding.bind(view)

    override fun getNavigator() = this
    override val viewModel by viewModels<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
        viewBinding?.run {
            settingsLogin.setOnClickListener {
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(
                    signInIntent,
                    RC_SIGN_IN
                )
            }
            settingsLogout.setOnClickListener {
                AuthUI.getInstance()
                    .signOut(requireContext())
                    .addOnCompleteListener {
                        viewModel.logout()
                        updateUI()
                    }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            try {
                updateUI()
            } catch (e: ApiException) {
                updateUI()
            }
        }
    }

    private fun updateUI() {

        viewBinding?.settingsAppVersion?.text = BuildConfig.VERSION_NAME
        val currencyUser = viewModel.getCurrentUser()
        if (currencyUser == null) {
            requireActivity().finish()
            startActivity(
                Intent(context, SplashActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        } else {
            viewBinding?.run {
                this.settingsAvatar.visibility = VISIBLE
                settingsName.visibility = VISIBLE
                settingsName.text = currencyUser.displayName
                GlideApp
                    .with(this@SettingsFragment)
                    .load(viewModel.getCurrentUser()?.photoUrl)
                    .centerCrop()
                    .into(settingsAvatar)
                settingsLogin.visibility = GONE
                settingsLogout.visibility = VISIBLE
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
