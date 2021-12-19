package com.d9tilov.moneymanager.exchanger.domain

import java.math.BigDecimal

interface ExchangeInteractor {

    suspend fun convertToMainCurrency(amount: BigDecimal, currencyCode: String): BigDecimal
    suspend fun toUsd(amount: BigDecimal, currencyCode: String): BigDecimal
}
