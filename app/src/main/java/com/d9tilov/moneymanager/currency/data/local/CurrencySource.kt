package com.d9tilov.moneymanager.currency.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.currency.data.entity.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencySource : Source {

    suspend fun saveCurrencies(currencies: List<Currency>)
    fun getCurrencies(): Flow<List<Currency>>
}
