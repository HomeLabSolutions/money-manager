package com.d9tilov.android.currency.data.impl

import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.toLocalDate
import com.d9tilov.android.currency.data.model.Currency
import com.d9tilov.android.currency.data.model.CurrencyMetaData
import com.d9tilov.android.currency.data.impl.mapper.toDataModel
import com.d9tilov.android.currency.data.impl.mapper.toDbModel
import com.d9tilov.android.database.dao.CurrencyListDao
import com.d9tilov.android.database.dao.MainCurrencyDao
import com.d9tilov.android.datastore.PreferencesStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map

class CurrencyLocalSource(
    private val preferencesStore: PreferencesStore,
    private val currencyListDao: CurrencyListDao,
    private val mainCurrencyDao: MainCurrencyDao
) : com.d9tilov.android.currency.data.contract.CurrencySource {

    override fun getMainCurrency(): Flow<CurrencyMetaData> =
        preferencesStore.uid
            .filterNotNull()
            .flatMapMerge { uid ->
                mainCurrencyDao.get(uid).map { it?.toDataModel() ?: CurrencyMetaData.EMPTY }
            }

    override suspend fun saveCurrencies(currencies: List<Currency>) {
        currencyListDao.insert(currencies.map { item -> item.toDbModel() }.toList())
    }

    override suspend fun getCurrencyByCode(code: String): Currency =
        currencyListDao.getByCode(code).toDataModel()

    override suspend fun hasAlreadyUpdatedToday(baseCurrency: String): Boolean =
        currencyListDao.getLastUpdateTime(baseCurrency).toLocalDate() == currentDate()

    override suspend fun update(currency: Currency) {
        currencyListDao.update(currency.toDbModel())
    }

    override suspend fun updateMainCurrency(code: String) {
        val uid = preferencesStore.uid.firstOrNull()
        uid?.let {
            mainCurrencyDao.insert(
                CurrencyMetaData(
                    it,
                    code,
                    code.getSymbolByCode()
                ).toDbModel()
            )
        }
    }

    override fun getCurrencies(): Flow<List<Currency>> {
        return currencyListDao.getAll().map { it.map { item -> item.toDataModel() } }
    }
}
