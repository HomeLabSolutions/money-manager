package com.d9tilov.android.profile.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.d9tilov.android.backup.ui.PeriodicBackupWorker
import com.d9tilov.android.common_android.ui.base.BaseDialogFragment
import com.d9tilov.android.profile.ui.navigation.LogoutDialogNavigator
import com.d9tilov.android.profile.ui.vm.LogoutViewModel
import com.d9tilov.android.profile_ui.databinding.FragmentDialogLogoutBinding
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogoutDialog :
    BaseDialogFragment<LogoutDialogNavigator, FragmentDialogLogoutBinding>(FragmentDialogLogoutBinding::inflate), LogoutDialogNavigator {

    override fun getNavigator() = this
    override val viewModel by viewModels<LogoutViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            logoutButtonConfirm.setOnClickListener {
                AuthUI.getInstance()
                    .signOut(requireContext())
                    .addOnCompleteListener { viewModel.logout() }
            }
            logoutButtonCancel.setOnClickListener { dismiss() }
        }
    }

    override fun logout() {
        PeriodicBackupWorker.stopPeriodicJob(requireContext())
        val intent = Intent()
        intent.setClassName(requireContext(), "com.d9tilov.android.splash.ui.RouterActivity")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        requireActivity().finish()
    }
}
