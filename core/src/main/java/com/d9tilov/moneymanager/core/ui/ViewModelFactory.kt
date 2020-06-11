package com.d9tilov.moneymanager.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T?>): T {
        val viewModelProvider: Provider<ViewModel> = viewModels[modelClass]
            ?: throw IllegalArgumentException("model $modelClass not found")
        return viewModelProvider.get() as T
    }
}
