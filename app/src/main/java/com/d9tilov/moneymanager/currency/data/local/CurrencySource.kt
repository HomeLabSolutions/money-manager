package com.d9tilov.moneymanager.currency.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.currency.data.entity.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencySource : Source {

    suspend fun saveCurrencies(currencies: List<Currency>)
    suspend fun getCurrencyByCode(code: String): Currency
    suspend fun isUsed(baseCurrency: String): Boolean
    suspend fun hasAlreadyUpdatedToday(baseCurrency: String): Boolean
    suspend fun update(currency: Currency)
    fun getCurrencies(): Flow<List<Currency>>
}
