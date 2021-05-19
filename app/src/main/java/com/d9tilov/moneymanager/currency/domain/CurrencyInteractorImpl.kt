package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.currency.domain.mapper.CurrencyDomainMapper
import com.d9tilov.moneymanager.user.domain.UserInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class CurrencyInteractorImpl(
    private val currencyRepo: CurrencyRepo,
    private val userInteractor: UserInteractor,
    private val domainMapper: CurrencyDomainMapper
) : CurrencyInteractor {

    override fun getCurrencies(): Flow<List<DomainCurrency>> {
        return flow { emit(userInteractor.getCurrency()) }
            .flatMapConcat { baseCurrency ->
                currencyRepo.getCurrencies(baseCurrency)
                    .map { list ->
                        list.map { item ->
                            domainMapper.toDomain(
                                item,
                                baseCurrency == item.code
                            )
                        }
                    }
            }
    }

    override suspend fun updateBaseCurrency(currency: DomainCurrency) {
        val user = userInteractor.getCurrentUser().first()
        userInteractor.updateUser(user.copy(currencyCode = currency.code))
        currencyRepo.updateBaseCurrency(domainMapper.toDataModel(currency))
    }
}
