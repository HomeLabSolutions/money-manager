package com.d9tilov.moneymanager.transaction.data.mapper

import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.android.database.entity.TransactionDbModel

fun TransactionDbModel.toDataModel(): TransactionDataModel = TransactionDataModel(
    id,
    clientId,
    type,
    categoryId,
    currency,
    sum,
    usdSum,
    date,
    description,
    qrCode,
    isRegular,
    inStatistics,
    location,
    photoUri
)

fun TransactionDataModel.toDbModel(): TransactionDbModel =
    TransactionDbModel(
        id,
        clientId,
        type,
        categoryId,
        currencyCode,
        sum,
        usdSum,
        date,
        description,
        qrCode,
        isRegular,
        inStatistics,
        location,
        photoUri
    )
