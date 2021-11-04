package com.d9tilov.moneymanager.currency.data.remote.mapper

import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.remote.entity.CurrencyResponse

fun CurrencyResponse.toDataModel(currencyMap: Map<String, String>): List<Currency> {
    val currencyRateList = mutableSetOf<Currency>()
    for ((key, value) in rates) {
        val item = Currency(
            code = key,
            value = value.toBigDecimal(),
            symbol = if (currencyMap.isEmpty()) key else currencyMap[key] ?: key
        )
        currencyRateList.add(item)
    }
    return currencyRateList.toList()
}
