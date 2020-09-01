package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.currency.data.entity.Currency

class CurrencyInteractorImpl(
    private val currencyRepo: CurrencyRepo
) : CurrencyInteractor {

    override fun getCurrencies() =
        currencyRepo.getCurrencies()

    override fun updateBaseCurrency(currency: Currency) =
        currencyRepo.updateBaseCurrency(currency)
}
