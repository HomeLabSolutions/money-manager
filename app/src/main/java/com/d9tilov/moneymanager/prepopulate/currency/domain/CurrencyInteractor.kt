package com.d9tilov.moneymanager.prepopulate.currency.domain

import com.d9tilov.moneymanager.prepopulate.currency.data.entity.Currency
import io.reactivex.Completable
import io.reactivex.Single

interface CurrencyInteractor {

    fun getCurrencies(): Single<List<Currency>>
    fun updateBaseCurrency(currency: Currency): Completable
}
