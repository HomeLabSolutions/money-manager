package com.d9tilov.moneymanager.prepopulate.currency.data.remote.mapper

import com.d9tilov.moneymanager.core.mapper.Mapper
import com.d9tilov.moneymanager.prepopulate.currency.data.entity.Currency
import com.d9tilov.moneymanager.prepopulate.currency.data.remote.entity.CurrencyResponse
import javax.inject.Inject

class CurrencyRemoteMapper @Inject constructor() : Mapper<CurrencyResponse, List<Currency>> {

    override fun toDataModel(model: CurrencyResponse): List<Currency> =
        with(model) {
            val currencyRateList = mutableSetOf<Currency>()
            for ((key, value) in rates) {
                val item = Currency(code = key, value = value, isBase = key == base)
                currencyRateList.add(item)
            }
            currencyRateList.toList()
        }

    override fun toDbModel(model: List<Currency>): CurrencyResponse {
        TODO("Not yet implemented")
    }
}
