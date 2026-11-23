package com.d9tilov.android.currency.data.impl.mapper

import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.toDecimal
import com.d9tilov.android.currency.data.contract.model.CurrencyResponse
import com.d9tilov.android.currency.domain.model.CurrencyRate

fun CurrencyResponse.toDataModel(): List<CurrencyRate> {
    val currencyRateList = mutableSetOf<CurrencyRate>()
    for ((key, value) in rates) {
        val item =
            CurrencyRate(
                code = key,
                symbol = key.getSymbolByCode(),
                value = value.content.toDecimal(),
                lastUpdateTime = date,
            )
        currencyRateList.add(item)
    }
    return currencyRateList.toList()
}

fun CurrencyResponse.toDataModelValues(list: List<CurrencyRate>): List<CurrencyRate> {
    val newList = mutableListOf<CurrencyRate>()
    list.forEach { currency ->
        val newCurrency =
            currency.copy(
                value = rates[currency.code]?.content?.toDecimal() ?: Decimal.ZERO,
                lastUpdateTime = date * MILLIS,
            )
        newList.add(newCurrency)
    }
    return newList
}

private const val MILLIS = 1000L
