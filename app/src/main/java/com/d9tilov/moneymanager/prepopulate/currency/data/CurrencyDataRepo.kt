package com.d9tilov.moneymanager.prepopulate.currency.data

import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.prepopulate.currency.data.entity.Currency
import com.d9tilov.moneymanager.prepopulate.currency.data.local.CurrencySource
import com.d9tilov.moneymanager.prepopulate.currency.data.remote.CurrencyApi
import com.d9tilov.moneymanager.prepopulate.currency.data.remote.mapper.CurrencyRemoteMapper
import com.d9tilov.moneymanager.prepopulate.currency.domain.CurrencyRepo
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Retrofit

class CurrencyDataRepo(
    private val preferencesStore: PreferencesStore,
    private val currencySource: CurrencySource,
    private val currencyRemoteMapper: CurrencyRemoteMapper,
    retrofit: Retrofit
) : CurrencyRepo {

    private val currencyApi = retrofit.create(CurrencyApi::class.java)

    override fun getCurrencies(): Single<List<Currency>> {
        val localSingle = currencySource.getCurrencies().toSingle()
            .map { list ->
                val a = list.map { it.copy(isBase = it.code == preferencesStore.baseCurrencyCode) }
                a
            }
        val remoteSingle = currencyApi.getCurrencies(preferencesStore.baseCurrencyCode)
            .map { currencyRemoteMapper.toDataModel(it) }
            .flatMap { currencySource.saveCurrencies(it).toSingleDefault(it) }
        return Single.concat(localSingle, remoteSingle)
            .filter { it.isNotEmpty() }
            .firstOrError()
    }

    override fun updateBaseCurrency(currency: Currency): Completable =
        Completable.fromAction { preferencesStore.baseCurrencyCode = currency.code }
}
