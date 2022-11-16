package com.d9tilov.moneymanager.currency.data.local

import com.d9tilov.moneymanager.core.util.currentDate
import com.d9tilov.moneymanager.core.util.toLocalDate
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.local.mapper.toDataModel
import com.d9tilov.moneymanager.currency.data.local.mapper.toDbModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrencyLocalSource(private val currencyDao: CurrencyDao) : CurrencySource {

    override suspend fun saveCurrencies(currencies: List<Currency>) {
        currencyDao.insert(currencies.map { item -> item.toDbModel() }.toList())
    }

    override suspend fun getCurrencyByCode(code: String): Currency = currencyDao.getByCode(code).toDataModel()

    override suspend fun hasAlreadyUpdatedToday(baseCurrency: String): Boolean =
        currencyDao.getLastUpdateTime(baseCurrency).toLocalDate() == currentDate()

    override suspend fun update(currency: Currency) {
        currencyDao.update(currency.toDbModel())
    }

    override fun getCurrencies(): Flow<List<Currency>> {
        return currencyDao.getAll().map { it.map { item -> item.toDataModel() } }
    }
}
