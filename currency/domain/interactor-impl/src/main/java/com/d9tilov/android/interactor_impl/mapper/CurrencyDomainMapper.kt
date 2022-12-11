package com.d9tilov.android.interactor_impl.mapper

import com.d9tilov.android.repo.model.Currency
import com.d9tilov.android.interactor.model.DomainCurrency
import javax.inject.Inject

class CurrencyDomainMapper @Inject constructor() {

    fun toDataModel(currencyDomain: com.d9tilov.android.interactor.model.DomainCurrency): com.d9tilov.android.repo.model.Currency =
        with(currencyDomain) {
            com.d9tilov.android.repo.model.Currency(
                code,
                symbol,
                value,
                lastUpdateTime
            )
        }

    fun toDomain(currency: com.d9tilov.android.repo.model.Currency, isBase: Boolean): com.d9tilov.android.interactor.model.DomainCurrency =
        with(currency) { DomainCurrency(code, symbol, value, isBase, lastUpdateTime) }
}
