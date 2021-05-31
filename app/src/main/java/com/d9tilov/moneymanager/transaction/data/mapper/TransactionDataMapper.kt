package com.d9tilov.moneymanager.transaction.data.mapper

import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.data.local.entity.TransactionDbModel

fun TransactionDbModel.toDataModel(): TransactionDataModel = TransactionDataModel(
    id,
    clientId,
    type,
    sum,
    categoryId,
    currency,
    date,
    description,
    qrCode,
    isRegular
)

fun TransactionDataModel.toDbModel(): TransactionDbModel =
    TransactionDbModel(
        id,
        clientId,
        type,
        sum,
        categoryId,
        currency,
        date,
        description,
        qrCode,
        false,
        isRegular
    )
