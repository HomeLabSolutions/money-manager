package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.currency.data.entity.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepo {

    fun getCurrencies(baseCurrency: String): Flow<List<Currency>>
    suspend fun updateBaseCurrency(currency: Currency)
}
