package com.d9tilov.moneymanager.regular.vm

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.d9tilov.moneymanager.base.ui.navigator.RegularTransactionCreatedNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreatedRegularTransactionViewModel @AssistedInject constructor(
    private val regularTransactionInteractor: RegularTransactionInteractor,
    @Assisted val savedStateHandle: SavedStateHandle
) : BaseViewModel<RegularTransactionCreatedNavigator>() {

    fun saveOrUpdate(transaction: RegularTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            regularTransactionInteractor.insert(transaction)
            withContext(Dispatchers.Main) { navigator?.back() }
        }
    }

    //                        dayOfWeek = if (_periodType.value != PeriodType.WEEK) 0 else _weekDaysSelected.value!!
    @AssistedFactory
    interface CreatedRegularTransactionViewModelFactory {
        fun create(handle: SavedStateHandle): CreatedRegularTransactionViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: CreatedRegularTransactionViewModelFactory,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null
        ): AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel?> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return assistedFactory.create(handle) as T
                }
            }
    }
}
