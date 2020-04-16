package com.d9tilov.moneymanager.splash.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class SplashViewModelFactory @Inject constructor() : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != SplashViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return SplashViewModel() as T
    }
}
