package com.d9tilov.moneymanager.transaction.data.mapper

import com.d9tilov.moneymanager.core.mapper.Mapper
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.data.local.entity.TransactionDbModel
import javax.inject.Inject

class TransactionDataMapper @Inject constructor() : Mapper<TransactionDbModel, TransactionDataModel> {
    override fun toDataModel(model: TransactionDbModel): TransactionDataModel =
        with(model) {
            TransactionDataModel(
                id,
                clientId,
                type,
                sum,
                categoryId,
                currency,
                date,
                description,
                qrCode
            )
        }

    override fun toDbModel(model: TransactionDataModel): TransactionDbModel =
        with(model) {
            TransactionDbModel(
                id,
                clientId,
                false,
                type,
                sum,
                categoryId,
                currency,
                date,
                description,
                qrCode
            )
        }
}
