package com.d9tilov.moneymanager.settings.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.d9tilov.moneymanager.backup.domain.BackupInteractor
import com.d9tilov.moneymanager.domain.user.UserInfoInteractor
import com.d9tilov.moneymanager.settings.domain.SettingsInteractor
import javax.inject.Inject

class SettingsViewModelFactory @Inject constructor(
    private val userInfoInteractor: UserInfoInteractor,
    private val backupInteractor: BackupInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != SettingsViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return SettingsViewModel(
            userInfoInteractor,
            backupInteractor
        ) as T
    }
}