package com.d9tilov.moneymanager.currency.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.currency.data.entity.Currency

interface CurrencySource : Source {

    suspend fun saveCurrencies(currencies: List<Currency>)
    suspend fun getCurrencies(): List<Currency>
}
