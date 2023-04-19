package com.d9tilov.moneymanager.profile.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.backup.PeriodicBackupWorker
import com.d9tilov.android.common_android.ui.base.BaseDialogFragment
import com.d9tilov.moneymanager.base.ui.navigator.LogoutDialogNavigator
import com.d9tilov.moneymanager.databinding.FragmentDialogLogoutBinding
import com.d9tilov.moneymanager.profile.ui.vm.LogoutViewModel
import com.d9tilov.moneymanager.splash.ui.RouterActivity
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
        startActivity(
            Intent(context, RouterActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        requireActivity().finish()
    }
}
