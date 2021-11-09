package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.currency.data.entity.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepo {

    fun getCurrencies(baseCurrency: String): Flow<List<Currency>>
    suspend fun getCurrencyByCode(code: String): Currency
    suspend fun updateCurrencies(baseCurrency: String)
    suspend fun updateCurrency(currency: Currency)
    suspend fun updateCurrentCurrency(currency: Currency)
    suspend fun isUsed(baseCurrency: String): Boolean
    suspend fun hasAlreadyUpdatedToday(baseCurrency: String): Boolean
}
