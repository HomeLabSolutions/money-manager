package com.d9tilov.moneymanager.incomeexpense.expense.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.d9tilov.moneymanager.category.domain.ICategoryInteractor
import javax.inject.Inject

class ExpenseViewModelFactory @Inject constructor(private val categoryInteractor: ICategoryInteractor) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != ExpenseViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return ExpenseViewModel(categoryInteractor) as T
    }
}
