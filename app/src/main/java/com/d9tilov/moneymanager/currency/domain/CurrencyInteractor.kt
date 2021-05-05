package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import io.reactivex.Completable
import io.reactivex.Single

interface CurrencyInteractor {

    fun getCurrencies(): Single<List<DomainCurrency>>
    fun updateBaseCurrency(currency: DomainCurrency): Completable
}
