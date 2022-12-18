package com.d9tilov.android.currency.domain.impl.mapper

import com.d9tilov.android.currency.data.model.Currency
import com.d9tilov.android.currency.domain.model.DomainCurrency

fun DomainCurrency.toDataModel(): Currency =
    with(this) {
        Currency(
            code,
            symbol,
            value,
            lastUpdateTime
        )
    }

fun Currency.toDomain(isBase: Boolean): DomainCurrency =
    with(this) { DomainCurrency(code, symbol, value, isBase, lastUpdateTime) }
