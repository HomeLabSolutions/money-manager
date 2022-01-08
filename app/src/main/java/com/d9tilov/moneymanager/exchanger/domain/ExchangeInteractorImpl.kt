package com.d9tilov.moneymanager.exchanger.domain

import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_CODE
import com.d9tilov.moneymanager.core.util.divideBy
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import java.math.BigDecimal
import java.util.Calendar

class ExchangeInteractorImpl(private val currencyInteractor: CurrencyInteractor) :
    ExchangeInteractor {

    override suspend fun convertToMainCurrency(
        amount: BigDecimal,
        currencyCode: String
    ): BigDecimal {
        val mainCurrencyCode = currencyInteractor.getCurrentCurrencyCode()
        if (currencyCode == mainCurrencyCode) return amount
        val mainCurrency = currencyInteractor.getCurrencyByCode(mainCurrencyCode)
        val currentCurrency = currencyInteractor.getCurrencyByCode(currencyCode)
        val c = Calendar.getInstance()
        c.timeInMillis = mainCurrency.lastUpdateTime
        val mainAbsoluteAmount = mainCurrency.value
        val currentAbsoluteAmount = currentCurrency.value
        return amount.multiply(mainAbsoluteAmount.divideBy(currentAbsoluteAmount))
    }

    override suspend fun toUsd(amount: BigDecimal, currencyCode: String): BigDecimal {
        val mainCurrencyCode = DEFAULT_CURRENCY_CODE
        val mainCurrency = currencyInteractor.getCurrencyByCode(mainCurrencyCode)
        val currentCurrency = currencyInteractor.getCurrencyByCode(currencyCode)
        val c = Calendar.getInstance()
        c.timeInMillis = mainCurrency.lastUpdateTime
        val currentAbsoluteAmount = currentCurrency.value
        return amount.divideBy(currentAbsoluteAmount)
    }
}
