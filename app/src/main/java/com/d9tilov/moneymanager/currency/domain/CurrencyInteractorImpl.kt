package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.currency.domain.mapper.CurrencyDomainMapper
import com.d9tilov.moneymanager.user.domain.UserInteractor
import kotlinx.coroutines.flow.map

class CurrencyInteractorImpl(
    private val currencyRepo: CurrencyRepo,
    private val userInteractor: UserInteractor,
    private val domainMapper: CurrencyDomainMapper
) : CurrencyInteractor {

    override suspend fun getCurrencies(): List<DomainCurrency> {
        val baseCurrency = userInteractor.getCurrency()
        return currencyRepo.getCurrencies(baseCurrency)
            .map { domainMapper.toDomain(it, baseCurrency == it.code) }
    }

    override suspend fun updateBaseCurrency(currency: DomainCurrency) {
        userInteractor.getCurrentUser().map {
            userInteractor.updateUser(it.copy(currencyCode = currency.code))
            currencyRepo.updateBaseCurrency(domainMapper.toDataModel(currency))
        }
    }
}
