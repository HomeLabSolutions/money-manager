package com.d9tilov.android.repo

import com.d9tilov.android.repo.model.Currency
import com.d9tilov.android.repo.model.CurrencyMetaData
import kotlinx.coroutines.flow.Flow

interface CurrencyRepo {

    fun getCurrencies(): Flow<List<Currency>>
    fun getMainCurrency(): Flow<CurrencyMetaData>
    suspend fun getCurrencyByCode(code: String): Currency
    suspend fun updateCurrencies()
    suspend fun updateMainCurrency(code: String)
    suspend fun hasAlreadyUpdatedToday(baseCurrency: String): Boolean
}
