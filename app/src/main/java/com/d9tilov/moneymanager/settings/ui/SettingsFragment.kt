package com.d9tilov.moneymanager.settings.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.BuildConfig
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.getBackupDate
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.FragmentSettingsBinding
import com.d9tilov.moneymanager.settings.ui.vm.SettingsViewModel
import com.d9tilov.moneymanager.splash.ui.SplashActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment :
    BaseFragment<SettingsNavigator>(R.layout.fragment_settings),
    SettingsNavigator {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val viewBinding by viewBinding(FragmentSettingsBinding::bind)

    override fun getNavigator() = this
    override val viewModel by viewModels<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
        viewBinding.run {
            settingsLogout.setOnClickListener {
                AuthUI.getInstance()
                    .signOut(requireContext())
                    .addOnCompleteListener {
                        viewModel.logout()
                        updateUI()
                    }
            }
            settingsBackup.setOnClickListener {
                viewModel.backup()
            }
            viewModel.backupData.observe(
                viewLifecycleOwner,
                {
                    if (it.lastBackupTimestamp == 0L) {
                        settingsBackupInfo.setText(R.string.backup_empty)
                    } else {
                        settingsBackupInfo.text =
                            getString(R.string.backup_info, it.lastBackupTimestamp.getBackupDate())
                    }
                }
            )
        }
    }

    private fun updateUI() {
        viewBinding.settingsAppVersion.text = BuildConfig.VERSION_NAME
        val currencyUser = viewModel.getCurrentUser()
        if (currencyUser == null) {
            startActivity(
                Intent(context, SplashActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
            requireActivity().finish()
        } else {
            viewBinding.run {
                this.settingsAvatar.show()
                settingsName.show()
                settingsName.text = currencyUser.displayName
                GlideApp
                    .with(this@SettingsFragment)
                    .load(viewModel.getCurrentUser()?.photoUrl)
                    .centerCrop()
                    .into(settingsAvatar)
                settingsLogout.show()
            }
        }
    }
}
