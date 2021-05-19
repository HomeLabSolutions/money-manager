package com.d9tilov.moneymanager.currency.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.local.mapper.CurrencyLocalMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrencyLocalSource(
    private val currencyLocalMapper: CurrencyLocalMapper,
    appDatabase: AppDatabase
) : CurrencySource {

    private val currencyDao = appDatabase.currencyDao()

    override suspend fun saveCurrencies(currencies: List<Currency>) {
        currencies.forEach { currencyDao.insert(currencyLocalMapper.toDbModel(it)) }
    }

    override fun getCurrencies(): Flow<List<Currency>> {
        return currencyDao.getAll()
            .map { it.map { item -> currencyLocalMapper.toDataModel(item) } }
    }
}
