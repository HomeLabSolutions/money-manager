package com.d9tilov.moneymanager.currency.data.remote.mapper

import com.d9tilov.moneymanager.core.util.CurrencyUtils.getSymbolByCode
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.remote.entity.CurrencyResponse
import java.math.BigDecimal

fun CurrencyResponse.toDataModel(): List<Currency> {
    val currencyRateList = mutableSetOf<Currency>()
    for ((key, value) in rates) {
        val item = Currency(
            code = key,
            symbol = key.getSymbolByCode(),
            value = value.toBigDecimal(),
            lastUpdateTime = date
        )
        currencyRateList.add(item)
    }
    return currencyRateList.toList()
}

fun CurrencyResponse.toDataModelValues(list: List<Currency>): List<Currency> {
    val newList = mutableListOf<Currency>()
    list.forEach { currency ->
        val newCurrency = currency.copy(
            value = rates[currency.code]?.toBigDecimal() ?: BigDecimal.ZERO,
            lastUpdateTime = date * MILLIS
        )
        newList.add(newCurrency)
    }
    return newList
}

private const val MILLIS = 1000L
