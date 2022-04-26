package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.base.data.local.preferences.CurrencyMetaData
import com.d9tilov.moneymanager.currency.data.entity.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepo {

    fun getCurrencies(): Flow<List<Currency>>
    suspend fun getCurrentCurrency(): CurrencyMetaData
    suspend fun getCurrencyByCode(code: String): Currency
    suspend fun updateCurrencies()
    suspend fun hasAlreadyUpdatedToday(baseCurrency: String): Boolean
}
