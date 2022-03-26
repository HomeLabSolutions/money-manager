package com.d9tilov.moneymanager.statistics.vm

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.d9tilov.moneymanager.base.ui.navigator.StatisticsDetailsNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.getEndOfDay
import com.d9tilov.moneymanager.core.util.getStartOfDay
import com.d9tilov.moneymanager.core.util.toLocalDateTime
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatisticsDetailsViewModel @AssistedInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle,
    private val transactionInteractor: TransactionInteractor
) : BaseViewModel<StatisticsDetailsNavigator>() {

    private val transactions = MutableLiveData<List<Transaction>>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            transactions.postValue(
                transactionInteractor.getTransactionsByCategory(
                    savedStateHandle.get<TransactionType>("transactionType")
                        ?: throw IllegalArgumentException("TransactionType is null"),
                    savedStateHandle.get<Category>("category")
                        ?: throw IllegalArgumentException("Category is null"),
                    savedStateHandle.get<Long>("start_period")?.toLocalDateTime()?.getStartOfDay()
                        ?: throw IllegalArgumentException("Start period is null"),
                    savedStateHandle.get<Long>("end_period")?.toLocalDateTime()?.getEndOfDay()
                        ?: throw IllegalArgumentException("End period is null"),
                    savedStateHandle.get<Boolean>("in_statistics") ?: true
                ).sortedByDescending { item -> item.date }
            )
        }
    }

    fun getTransactions(): LiveData<List<Transaction>> = transactions

    @AssistedFactory
    interface StatisticsDetailsViewModelFactory {
        fun create(handle: SavedStateHandle): StatisticsDetailsViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: StatisticsDetailsViewModelFactory,
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