package com.d9tilov.moneymanager.category.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.d9tilov.moneymanager.category.domain.ICategoryInteractor
import javax.inject.Inject

class CategoryViewModelFactory @Inject constructor(
    private val categoryInteractor: ICategoryInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != CategoryViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return CategoryViewModel(categoryInteractor) as T
    }
}
