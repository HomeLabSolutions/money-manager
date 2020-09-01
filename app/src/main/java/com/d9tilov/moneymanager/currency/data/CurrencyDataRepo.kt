package com.d9tilov.moneymanager.currency.data

import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.local.CurrencySource
import com.d9tilov.moneymanager.currency.data.remote.CurrencyApi
import com.d9tilov.moneymanager.currency.data.remote.mapper.CurrencyRemoteMapper
import com.d9tilov.moneymanager.currency.domain.CurrencyRepo
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
                list.map { it.copy(isBase = it.code == preferencesStore.baseCurrencyCode) }
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
