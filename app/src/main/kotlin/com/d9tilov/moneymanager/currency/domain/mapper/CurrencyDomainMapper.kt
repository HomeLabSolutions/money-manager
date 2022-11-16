package com.d9tilov.moneymanager.currency.domain.mapper

import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import javax.inject.Inject

class CurrencyDomainMapper @Inject constructor() {

    fun toDataModel(currencyDomain: DomainCurrency): Currency =
        with(currencyDomain) { Currency(code, symbol, value, lastUpdateTime) }

    fun toDomain(currency: Currency, isBase: Boolean): DomainCurrency =
        with(currency) { DomainCurrency(code, symbol, value, isBase, lastUpdateTime) }
}
