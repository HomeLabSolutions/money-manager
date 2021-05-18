package com.d9tilov.moneymanager.profile.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.BuildConfig
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.ProfileNavigator
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.getBackupDate
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.FragmentProfileBinding
import com.d9tilov.moneymanager.profile.ui.vm.ProfileViewModel
import com.d9tilov.moneymanager.splash.ui.SplashActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<ProfileNavigator>(R.layout.fragment_profile),
    ProfileNavigator {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val viewBinding by viewBinding(FragmentProfileBinding::bind)

    override fun getNavigator() = this
    override val viewModel by viewModels<ProfileViewModel>()

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
            profileLogout.setOnClickListener {
                AuthUI.getInstance()
                    .signOut(requireContext())
                    .addOnCompleteListener {
                        viewModel.logout()
                        updateUI()
                    }
            }
            profileBackup.setOnClickListener {
                viewModel.backup()
            }
            viewModel.backupData.observe(
                viewLifecycleOwner,
                {
                    if (it.lastBackupTimestamp == 0L) {
                        profileBackupInfo.setText(R.string.backup_empty)
                    } else {
                        profileBackupInfo.text =
                            getString(R.string.backup_info, it.lastBackupTimestamp.getBackupDate())
                    }
                }
            )
        }
    }

    private fun updateUI() {
        viewBinding.profileAppVersion.text = BuildConfig.VERSION_NAME
        val currencyUser = viewModel.getCurrentUser()
        if (currencyUser == null) {
            startActivity(
                Intent(context, SplashActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
            requireActivity().finish()
        } else {
            viewBinding.run {
                this.profileAvatar.show()
                profileName.show()
                profileName.text = currencyUser.displayName
                GlideApp
                    .with(this@ProfileFragment)
                    .load(viewModel.getCurrentUser()?.photoUrl)
                    .centerCrop()
                    .into(profileAvatar)
                profileLogout.show()
            }
        }
    }
}
