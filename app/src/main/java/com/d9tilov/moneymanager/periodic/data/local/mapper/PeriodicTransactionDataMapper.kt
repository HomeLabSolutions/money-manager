package com.d9tilov.moneymanager.periodic.data.local.mapper

import com.d9tilov.moneymanager.core.mapper.Mapper
import com.d9tilov.moneymanager.periodic.data.entity.PeriodicTransactionData
import com.d9tilov.moneymanager.periodic.data.local.entity.PeriodicTransactionDbModel
import javax.inject.Inject

class PeriodicTransactionDataMapper @Inject constructor() :
    Mapper<PeriodicTransactionDbModel, PeriodicTransactionData> {

    override fun toDataModel(model: PeriodicTransactionDbModel): PeriodicTransactionData =
        with(model) {
            PeriodicTransactionData(
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

    override fun toDbModel(model: PeriodicTransactionData): PeriodicTransactionDbModel =
        with(model) {
            PeriodicTransactionDbModel(
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
