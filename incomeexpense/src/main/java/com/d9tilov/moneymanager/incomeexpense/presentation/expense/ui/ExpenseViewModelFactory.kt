package com.d9tilov.moneymanager.incomeexpense.presentation.expense.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class ExpenseViewModelFactory @Inject constructor(
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != ExpenseViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return ExpenseViewModel() as T
    }
}
