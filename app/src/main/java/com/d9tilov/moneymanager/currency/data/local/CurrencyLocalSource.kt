package com.d9tilov.moneymanager.currency.data.local

import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.util.isSameDay
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.local.mapper.toDataModel
import com.d9tilov.moneymanager.currency.data.local.mapper.toDbModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class CurrencyLocalSource(private val currencyDao: CurrencyDao) : CurrencySource {

    override suspend fun saveCurrencies(currencies: List<Currency>) {
        currencies.forEach { currency ->
            if (currency.code == DataConstants.DEFAULT_CURRENCY_CODE)
                currencyDao.insert(currency.toDbModel().copy(used = true))
            else currencyDao.insert(currency.toDbModel())
        }
    }

    override suspend fun getCurrencyByCode(code: String): Currency = currencyDao.getByCode(code).toDataModel()

    override suspend fun isUsed(baseCurrency: String): Boolean =
        if (baseCurrency == DataConstants.DEFAULT_CURRENCY_CODE) true else currencyDao.isUsed(
            baseCurrency
        )

    override suspend fun hasAlreadyUpdatedToday(baseCurrency: String): Boolean =
        Date(currencyDao.getLastUpdateTime(baseCurrency)).isSameDay(Date())

    override suspend fun update(currency: Currency) {
        currencyDao.update(currency.toDbModel())
    }

    override fun getCurrencies(): Flow<List<Currency>> {
        return currencyDao.getAll().map { it.map { item -> item.toDataModel() } }
    }
}
