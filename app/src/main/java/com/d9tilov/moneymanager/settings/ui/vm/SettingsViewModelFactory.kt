package com.d9tilov.moneymanager.settings.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.d9tilov.moneymanager.category.ICategoryInteractor
import com.d9tilov.moneymanager.settings.domain.ISettingsInteractor
import com.d9tilov.moneymanager.user.domain.IUserInfoInteractor
import javax.inject.Inject

class SettingsViewModelFactory @Inject constructor(
    private val userInfoInteractor: IUserInfoInteractor,
    private val categoryInteractor: ICategoryInteractor,
    private val settingsInteractor: ISettingsInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != SettingsViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return SettingsViewModel(userInfoInteractor, categoryInteractor, settingsInteractor) as T
    }
}
