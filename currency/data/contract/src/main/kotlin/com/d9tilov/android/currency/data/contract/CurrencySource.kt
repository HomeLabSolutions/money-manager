package com.d9tilov.android.currency.data.contract

import com.d9tilov.android.currency.domain.model.Currency
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import kotlinx.coroutines.flow.Flow

interface CurrencySource {
    fun getMainCurrency(): Flow<CurrencyMetaData>

    suspend fun saveCurrencies(currencies: List<Currency>)

    suspend fun getCurrencyByCode(code: String): Currency

    suspend fun hasAlreadyUpdatedToday(baseCurrency: String): Boolean

    suspend fun update(currency: Currency)

    suspend fun updateMainCurrency(code: String)

    fun getCurrencies(): Flow<List<Currency>>
}
