package com.d9tilov.moneymanager.settings.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class SettingsViewModelFactory @Inject constructor(
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != SettingsViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return SettingsViewModel() as T
    }
}