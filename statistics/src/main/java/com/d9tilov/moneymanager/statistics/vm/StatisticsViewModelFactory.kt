package com.d9tilov.moneymanager.statistics.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class StatisticsViewModelFactory @Inject constructor(
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != StatisticsViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return StatisticsViewModel() as T
    }
}