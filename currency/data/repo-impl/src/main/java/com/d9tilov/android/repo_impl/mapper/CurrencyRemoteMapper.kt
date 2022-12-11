@file:Suppress("PackageNaming")

package com.d9tilov.android.repo_impl.mapper

import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.repo.model.Currency
import com.d9tilov.android.network.model.CurrencyResponse
import java.math.BigDecimal

fun CurrencyResponse.toDataModel(): List<com.d9tilov.android.repo.model.Currency> {
    val currencyRateList = mutableSetOf<com.d9tilov.android.repo.model.Currency>()
    for ((key, value) in rates) {
        val item = com.d9tilov.android.repo.model.Currency(
            code = key,
            symbol = key.getSymbolByCode(),
            value = value.toBigDecimal(),
            lastUpdateTime = date
        )
        currencyRateList.add(item)
    }
    return currencyRateList.toList()
}

fun CurrencyResponse.toDataModelValues(list: List<com.d9tilov.android.repo.model.Currency>): List<com.d9tilov.android.repo.model.Currency> {
    val newList = mutableListOf<com.d9tilov.android.repo.model.Currency>()
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
