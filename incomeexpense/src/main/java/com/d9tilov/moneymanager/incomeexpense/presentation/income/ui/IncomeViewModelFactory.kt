package com.d9tilov.moneymanager.incomeexpense.presentation.income.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class IncomeViewModelFactory @Inject constructor(
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != IncomeViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return IncomeViewModel() as T
    }
}
