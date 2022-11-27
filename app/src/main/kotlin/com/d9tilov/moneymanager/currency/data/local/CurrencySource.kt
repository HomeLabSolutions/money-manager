package com.d9tilov.moneymanager.currency.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.entity.CurrencyMetaData
import kotlinx.coroutines.flow.Flow

interface CurrencySource : Source {

    fun getMainCurrency(): Flow<CurrencyMetaData>
    suspend fun saveCurrencies(currencies: List<Currency>)
    suspend fun getCurrencyByCode(code: String): Currency
    suspend fun hasAlreadyUpdatedToday(baseCurrency: String): Boolean
    suspend fun update(currency: Currency)
    suspend fun updateMainCurrency(code: String)
    fun getCurrencies(): Flow<List<Currency>>
}
