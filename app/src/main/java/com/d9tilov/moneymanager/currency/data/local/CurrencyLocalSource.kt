package com.d9tilov.moneymanager.currency.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.local.mapper.CurrencyLocalMapper

class CurrencyLocalSource(
    private val currencyLocalMapper: CurrencyLocalMapper,
    appDatabase: AppDatabase
) : CurrencySource {

    private val currencyDao = appDatabase.currencyDao()

    override suspend fun saveCurrencies(currencies: List<Currency>) {
        currencies.forEach { currencyDao.insert(currencyLocalMapper.toDbModel(it)) }
    }

    override suspend fun getCurrencies() = currencyDao.getAll()
        .map {
            currencyLocalMapper.toDataModel(it)
        }
}
