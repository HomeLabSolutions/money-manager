package com.d9tilov.moneymanager.currency.data.local

import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.local.mapper.toDataModel
import com.d9tilov.moneymanager.currency.data.local.mapper.toDbModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrencyLocalSource(private val currencyDao: CurrencyDao) : CurrencySource {

    override suspend fun saveCurrencies(currencies: List<Currency>) {
        currencies.forEach { currencyDao.insert(it.toDbModel()) }
    }

    override fun getCurrencies(): Flow<List<Currency>> {
        return currencyDao.getAll().map { it.map { item -> item.toDataModel() } }
    }
}
