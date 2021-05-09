package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency

interface CurrencyInteractor {

    suspend fun getCurrencies(): List<DomainCurrency>
    suspend fun updateBaseCurrency(currency: DomainCurrency)
}
