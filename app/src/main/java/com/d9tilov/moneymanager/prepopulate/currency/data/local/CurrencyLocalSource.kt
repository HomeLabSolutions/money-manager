package com.d9tilov.moneymanager.prepopulate.currency.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.prepopulate.currency.data.entity.Currency
import com.d9tilov.moneymanager.prepopulate.currency.data.local.mapper.CurrencyLocalMapper
import io.reactivex.Completable
import io.reactivex.Observable

class CurrencyLocalSource(
    private val preferencesStore: PreferencesStore,
    private val currencyLocalMapper: CurrencyLocalMapper,
    appDatabase: AppDatabase
) : CurrencySource {

    private val currencyDao = appDatabase.currencyDao()

    override fun saveCurrencies(currencies: List<Currency>): Completable {
        return Observable.fromIterable(currencies)
            .flatMapCompletable { currencyDao.insert(currencyLocalMapper.toDbModel(it)) }
    }

    override fun getCurrencies() = currencyDao.getAll()
        .map { list ->
            list.map {
                currencyLocalMapper.toDataModel(it)
                    .copy(isBase = it.code == preferencesStore.baseCurrencyCode)
            }
        }
}
