package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.currency.data.entity.Currency

interface CurrencyRepo {

    suspend fun getCurrencies(baseCurrency: String): List<Currency>
    suspend fun updateBaseCurrency(currency: Currency)
}
