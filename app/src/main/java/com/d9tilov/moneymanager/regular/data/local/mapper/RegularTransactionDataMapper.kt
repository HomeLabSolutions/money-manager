package com.d9tilov.moneymanager.regular.data.local.mapper

import com.d9tilov.moneymanager.core.mapper.Mapper
import com.d9tilov.moneymanager.regular.data.entity.RegularTransactionData
import com.d9tilov.moneymanager.regular.data.local.entity.RegularTransactionDbModel
import javax.inject.Inject

class RegularTransactionDataMapper @Inject constructor() :
    Mapper<RegularTransactionDbModel, RegularTransactionData> {

    override fun toDataModel(model: RegularTransactionDbModel): RegularTransactionData =
        with(model) {
            RegularTransactionData(
                id,
                clientId,
                currency,
                type,
                sum,
                categoryId,
                createdDate,
                startDate,
                periodType,
                dayOfWeek,
                description,
                pushEnable
            )
        }

    override fun toDbModel(model: RegularTransactionData): RegularTransactionDbModel =
        with(model) {
            RegularTransactionDbModel(
                id,
                clientId,
                currencyCode,
                type,
                sum,
                categoryId,
                createdDate,
                startDate,
                periodType,
                dayOfWeek,
                description,
                pushEnable
            )
        }
}
