package com.d9tilov.moneymanager.profile.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.backup.PeriodicBackupWorker
import com.d9tilov.moneymanager.base.ui.BaseDialogFragment
import com.d9tilov.moneymanager.base.ui.navigator.LogoutDialogNavigator
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.databinding.FragmentDialogLogoutBinding
import com.d9tilov.moneymanager.profile.ui.vm.LogoutViewModel
import com.d9tilov.moneymanager.splash.ui.SplashActivity
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogoutDialog :
    BaseDialogFragment<LogoutDialogNavigator>(), LogoutDialogNavigator {

    private val viewBinding by viewBinding(FragmentDialogLogoutBinding::bind)

    override val layoutId = R.layout.fragment_dialog_logout
    override fun getNavigator() = this
    override val viewModel by viewModels<LogoutViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.run {
            logoutButtonConfirm.setOnClickListener {
                AuthUI.getInstance()
                    .signOut(requireContext())
                    .addOnCompleteListener {
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            ARG_LOGOUT_CANCEL_JOB,
                            true
                        )
                        viewModel.logout()
                    }
            }
            logoutButtonCancel.setOnClickListener { dismiss() }
        }
    }

    override fun logout() {
        PeriodicBackupWorker.stopPeriodicJob(requireContext())
        startActivity(
            Intent(context, SplashActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        requireActivity().finish()
    }

    companion object {
        const val ARG_LOGOUT_CANCEL_JOB = "arg_logout_cancel_job"
    }
}
