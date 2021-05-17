package com.d9tilov.moneymanager.currency.data

import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.local.CurrencySource
import com.d9tilov.moneymanager.currency.data.remote.CurrencyApi
import com.d9tilov.moneymanager.currency.data.remote.mapper.CurrencyRemoteMapper
import com.d9tilov.moneymanager.currency.domain.CurrencyRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit

class CurrencyDataRepo(
    private val preferencesStore: PreferencesStore,
    private val currencySource: CurrencySource,
    private val currencyRemoteMapper: CurrencyRemoteMapper,
    retrofit: Retrofit
) : CurrencyRepo {

    private val currencyApi = retrofit.create(CurrencyApi::class.java)

    override fun getCurrencies(baseCurrency: String): Flow<List<Currency>> {
        return currencySource.getCurrencies().map {
            if (it.isEmpty()) {
                val remoteCurrencyList =
                    currencyRemoteMapper.toDataModel(currencyApi.getCurrencies(preferencesStore.baseCurrencyCode))
                currencySource.saveCurrencies(remoteCurrencyList)
            }
            it
        }
    }

    override suspend fun updateBaseCurrency(currency: Currency) {
        preferencesStore.baseCurrencyCode = currency.code
    }
}
