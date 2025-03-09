package com.d9tilov.android.currency.domain.contract.mapper

import com.d9tilov.android.currency.domain.model.Currency
import com.d9tilov.android.currency.domain.model.DomainCurrency

fun DomainCurrency.toDataModel(): Currency =
    with(this) {
        Currency(
            code,
            symbol,
            value,
            lastUpdateTime,
        )
    }

fun Currency.toDomain(isBase: Boolean): DomainCurrency =
    with(this) {
        DomainCurrency(code, symbol, value, isBase, lastUpdateTime)
    }
