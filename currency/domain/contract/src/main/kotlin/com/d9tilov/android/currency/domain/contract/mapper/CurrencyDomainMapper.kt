package com.d9tilov.android.currency.domain.contract.mapper

import com.d9tilov.android.currency.domain.model.CurrencyRate
import com.d9tilov.android.currency.domain.model.DomainCurrency

fun DomainCurrency.toDataModel(): CurrencyRate =
    with(this) {
        CurrencyRate(
            code,
            symbol,
            value,
            lastUpdateTime,
        )
    }

fun CurrencyRate.toDomain(isBase: Boolean): DomainCurrency =
    with(this) {
        DomainCurrency(code, symbol, value, isBase, lastUpdateTime)
    }
