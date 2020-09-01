package com.d9tilov.moneymanager.fixed.data.local.mapper

import com.d9tilov.moneymanager.core.mapper.Mapper
import com.d9tilov.moneymanager.fixed.data.entity.FixedTransactionData
import com.d9tilov.moneymanager.fixed.data.local.entity.FixedTransactionDbModel
import javax.inject.Inject

class FixedTransactionDataMapper @Inject constructor() : Mapper<FixedTransactionDbModel, FixedTransactionData> {

    override fun toDataModel(model: FixedTransactionDbModel): FixedTransactionData =
        with(model) {
            FixedTransactionData(
                id,
                clientId,
                currency,
                type,
                sum,
                categoryId,
                createdDate,
                expireDate,
                description,
                pushEnable
            )
        }

    override fun toDbModel(model: FixedTransactionData): FixedTransactionDbModel =
        with(model) {
            FixedTransactionDbModel(
                id,
                clientId,
                currencyCode,
                type,
                sum,
                categoryId,
                createdDate,
                expireDate,
                description,
                pushEnable
            )
        }
}
