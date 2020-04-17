package com.d9tilov.moneymanager.settings

import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import com.d9tilov.moneymanager.BuildConfig
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseActivity
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.databinding.FragmentSettingsBinding
import com.d9tilov.moneymanager.splash.ui.SplashActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var baseActivity: BaseActivity? = null
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var accountManager: AccountManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.baseActivity = activity as BaseActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        auth = FirebaseAuth.getInstance()
        accountManager = AccountManager.get(requireContext())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        baseActivity?.setProgressBar(R.id.settings_progress_bar)
        updateUI()
        binding.settingsLogin.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(
                signInIntent,
                RC_SIGN_IN
            )
        }
        binding.settingsLogout.setOnClickListener {
            AuthUI.getInstance()
                .signOut(requireContext())
                .addOnCompleteListener {
                    updateUI()
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
        binding.settingsAppVersion.text = BuildConfig.VERSION_NAME
        val currencyUser = auth.currentUser
        if (currencyUser == null) {
            requireActivity().finish()
            startActivity(
                Intent(context, SplashActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        } else {
            with(binding) {
                this.settingsAvatar.visibility = VISIBLE
                settingsName.visibility = VISIBLE
                settingsName.text = currencyUser.displayName
                GlideApp
                    .with(this@SettingsFragment)
                    .load(auth.currentUser?.photoUrl)
                    .centerCrop()
                    .into(settingsAvatar)
                settingsLogin.visibility = GONE
                settingsLogout.visibility = VISIBLE
            }
        }
    }

    private fun showProgress() {
        baseActivity?.showProgressBar()
    }

    private fun hideProgress() {
        baseActivity?.hideProgressBar()
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}