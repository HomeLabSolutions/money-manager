package com.d9tilov.moneymanager.currency.data.local.mapper

import com.d9tilov.moneymanager.core.mapper.Mapper
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.local.entity.CurrencyDbModel
import javax.inject.Inject

class CurrencyLocalMapper @Inject constructor() : Mapper<CurrencyDbModel, Currency> {

    override fun toDataModel(model: CurrencyDbModel): Currency =
        with(model) {
            Currency(id, code, value)
        }

    override fun toDbModel(model: Currency): CurrencyDbModel =
        with(model) {
            CurrencyDbModel(id, code, value)
        }
}
