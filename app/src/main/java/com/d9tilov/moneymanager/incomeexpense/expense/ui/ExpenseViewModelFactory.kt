package com.d9tilov.moneymanager.incomeexpense.expense.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.d9tilov.moneymanager.backup.domain.BackupInteractor
import com.d9tilov.moneymanager.settings.domain.SettingsInteractor
import javax.inject.Inject

class ExpenseViewModelFactory @Inject constructor(
    private val backupInteractor: BackupInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != ExpenseViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return ExpenseViewModel(backupInteractor) as T
    }
}
