package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface CurrencyInteractor {

    fun getCurrencies(): Flow<List<DomainCurrency>>
    suspend fun getCurrentCurrencyCode(): String
    suspend fun getCurrencyByCode(code: String): Currency
    suspend fun toMainCurrency(amount: BigDecimal, currencyCode: String): BigDecimal
    suspend fun toUsd(amount: BigDecimal, currencyCode: String): BigDecimal
    suspend fun updateCurrencyRates()
    suspend fun updateCurrency(currency: Currency)
    suspend fun updateCurrentCurrency(currency: DomainCurrency)
}
