package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import kotlinx.coroutines.flow.Flow

interface CurrencyInteractor {

    fun getCurrencies(): Flow<List<DomainCurrency>>
    suspend fun updateCurrentCurrency(currency: DomainCurrency)
}
