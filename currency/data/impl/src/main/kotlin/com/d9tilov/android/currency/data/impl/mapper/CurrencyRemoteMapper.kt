package com.d9tilov.android.currency.data.impl.mapper

import com.d9tilov.android.core.model.Currency
import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.toDecimal
import com.d9tilov.android.currency.data.contract.model.CurrencyResponse
import com.d9tilov.android.currency.domain.model.CurrencyRate

fun CurrencyResponse.toDataModel(): List<CurrencyRate> =
    rates
        .filterKeys { Currency.fromCode(it) != null }
        .map { (code, rate) ->
            CurrencyRate(
                code = code,
                symbol = code.getSymbolByCode(),
                value = rate.content.toDecimal(),
                lastUpdateTime = date,
            )
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
