package com.d9tilov.moneymanager.currency.data

import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.local.CurrencySource
import com.d9tilov.moneymanager.currency.data.remote.CurrencyApi
import com.d9tilov.moneymanager.currency.data.remote.mapper.CurrencyRemoteMapper
import com.d9tilov.moneymanager.currency.domain.CurrencyRepo
import retrofit2.Retrofit

class CurrencyDataRepo(
    private val preferencesStore: PreferencesStore,
    private val currencySource: CurrencySource,
    private val currencyRemoteMapper: CurrencyRemoteMapper,
    retrofit: Retrofit
) : CurrencyRepo {

    private val currencyApi = retrofit.create(CurrencyApi::class.java)

    override suspend fun getCurrencies(baseCurrency: String): List<Currency> {
        val currencies = currencySource.getCurrencies()
        if (currencies.isEmpty()) {
            val remoteCurrencyList =
                currencyRemoteMapper.toDataModel(currencyApi.getCurrencies(preferencesStore.baseCurrencyCode))
            currencySource.saveCurrencies(remoteCurrencyList)
            return remoteCurrencyList
        }
        return currencies
    }

    override suspend fun updateBaseCurrency(currency: Currency) {
        preferencesStore.baseCurrencyCode = currency.code
    }
}
