package com.d9tilov.android.repo_impl

import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.repo.model.Currency
import com.d9tilov.android.repo.model.CurrencyMetaData
import com.d9tilov.android.network.CurrencyApi
import com.d9tilov.android.repo.CurrencyCacheSource
import com.d9tilov.android.repo.CurrencyRepo
import com.d9tilov.android.repo.CurrencySource
import com.d9tilov.android.repo_impl.mapper.toDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class CurrencyDataRepo(
    private val currencySource: com.d9tilov.android.repo.CurrencySource,
    private val currencyCacheSource: com.d9tilov.android.repo.CurrencyCacheSource,
    private val currencyApi: CurrencyApi,
    private val preferencesStore: PreferencesStore
) : com.d9tilov.android.repo.CurrencyRepo {

    override fun getCurrencies(): Flow<List<com.d9tilov.android.repo.model.Currency>> = currencySource.getCurrencies()
    override fun getMainCurrency(): Flow<com.d9tilov.android.repo.model.CurrencyMetaData> {
        return currencyCacheSource.getCurrentCurrency().map { currency ->
            if (currency == null) {
                val currencyFromDb =
                    currencySource.getMainCurrency().firstOrNull() ?: com.d9tilov.android.repo.model.CurrencyMetaData.EMPTY
                currencyCacheSource.setCurrentCurrency(currencyFromDb)
                currencyFromDb
            } else currency
        }
    }

    override suspend fun getCurrencyByCode(code: String): com.d9tilov.android.repo.model.Currency =
        currencySource.getCurrencyByCode(code)

    override suspend fun updateCurrencies() {
        val currencies = currencyApi.getCurrencies()
        val remoteCurrencyList = currencies.toDataModel()
        currencySource.saveCurrencies(remoteCurrencyList)
    }

    override suspend fun updateMainCurrency(code: String) {
        val uid = preferencesStore.uid.firstOrNull()
        uid?.let { clientUid ->
            currencyCacheSource.setCurrentCurrency(
                com.d9tilov.android.repo.model.CurrencyMetaData(
                    clientUid,
                    code,
                    code.getSymbolByCode()
                )
            )
            currencySource.updateMainCurrency(code)
        }
    }

    override suspend fun hasAlreadyUpdatedToday(baseCurrency: String): Boolean =
        currencySource.hasAlreadyUpdatedToday(baseCurrency)
}
