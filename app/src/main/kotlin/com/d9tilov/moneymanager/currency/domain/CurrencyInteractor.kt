package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.base.domain.Interactor
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface CurrencyInteractor : Interactor {

    fun getCurrencies(): Flow<List<DomainCurrency>>
    suspend fun getCurrencyByCode(code: String): Currency
    suspend fun toTargetCurrency(amount: BigDecimal, sourceCurrencyCode: String, targetCurrencyCode: String): BigDecimal
    suspend fun toUsd(amount: BigDecimal, currencyCode: String): BigDecimal
    suspend fun updateCurrencyRates(): Boolean
}
