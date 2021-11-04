package com.d9tilov.moneymanager.currency.data

import android.content.Context
import com.beust.klaxon.Klaxon
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.local.CurrencySource
import com.d9tilov.moneymanager.currency.data.local.entity.CurrencyJsonModel
import com.d9tilov.moneymanager.currency.data.local.entity.CurrencyListJsonModel
import com.d9tilov.moneymanager.currency.data.remote.CurrencyApi
import com.d9tilov.moneymanager.currency.data.remote.mapper.toDataModel
import com.d9tilov.moneymanager.currency.domain.CurrencyRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class CurrencyDataRepo(
    private val context: Context,
    private val preferencesStore: PreferencesStore,
    private val currencySource: CurrencySource,
    private val currencyApi: CurrencyApi
) : CurrencyRepo {

    override fun getCurrencies(baseCurrency: String): Flow<List<Currency>> {
        return currencySource.getCurrencies().map {
            if (it.isEmpty()) {
                val currencies = currencyApi.getCurrencies(preferencesStore.baseCurrencyCode)
                var currencyCodeList = listOf<CurrencyJsonModel>()
                val json = getJsonFromFile()
                if (json != null) {
                    currencyCodeList = Klaxon().parse<CurrencyListJsonModel>(json)?.currencies
                        ?: emptyList()
                }
                val currencyMap = mutableMapOf<String, String>()
                currencyCodeList.forEach { item -> currencyMap[item.code] = item.symbolNative }
                val remoteCurrencyList = currencies.toDataModel(currencyMap)
                currencySource.saveCurrencies(remoteCurrencyList)
            }
            it
        }
    }

    private fun getJsonFromFile(): String? {
        val json: String? = try {
            val `is`: InputStream = context.assets.open("currency_code.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    override suspend fun updateBaseCurrency(currency: Currency) {
        preferencesStore.baseCurrencyCode = currency.code
        preferencesStore.baseCurrencySymbol = currency.symbol
    }
}
