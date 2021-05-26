package com.d9tilov.moneymanager.currency.data.remote.mapper

import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.remote.entity.CurrencyResponse

fun CurrencyResponse.toDataModel(): List<Currency> {
    val currencyRateList = mutableSetOf<Currency>()
    for ((key, value) in rates) {
        val item = Currency(code = key, value = value.toBigDecimal())
        currencyRateList.add(item)
    }
    return currencyRateList.toList()
}
