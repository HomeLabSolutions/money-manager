package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.currency.domain.mapper.CurrencyDomainMapper
import com.d9tilov.moneymanager.user.domain.UserInteractor
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class CurrencyInteractorImpl(
    private val currencyRepo: CurrencyRepo,
    private val userInteractor: UserInteractor,
    private val domainMapper: CurrencyDomainMapper
) : CurrencyInteractor {

    override fun getCurrencies(): Single<List<DomainCurrency>> = userInteractor.getCurrentUser()
        .firstOrError()
        .flatMap { user ->
            currencyRepo.getCurrencies(user.currencyCode)
                .flatMap {
                    Observable.fromIterable(it)
                        .map { domainMapper.toDomain(it, user.currencyCode == it.code) }
                        .toList()
                }
        }

    override fun updateBaseCurrency(currency: DomainCurrency): Completable =
        userInteractor.getCurrentUser().firstOrError()
            .flatMapCompletable { userInteractor.updateUser(it.copy(currencyCode = currency.code)) }
}
