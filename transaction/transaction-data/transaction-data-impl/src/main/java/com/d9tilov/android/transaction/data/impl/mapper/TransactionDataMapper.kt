package com.d9tilov.android.transaction.data.impl.mapper

import com.d9tilov.android.core.model.toType
import com.d9tilov.android.database.entity.TransactionDbModel
import com.d9tilov.android.transaction.data.model.TransactionDataModel

fun TransactionDbModel.toDataModel(): TransactionDataModel =
    TransactionDataModel(
        id,
        clientId,
        type.toType(),
        categoryId,
        currency,
        sum,
        usdSum,
        date,
        description,
        qrCode,
        isRegular,
        inStatistics,
        latitude,
        longitude,
        photoUri
    )

fun TransactionDataModel.toDbModel(): TransactionDbModel =
    TransactionDbModel(
        id,
        clientId,
        type.value,
        categoryId,
        currencyCode,
        sum,
        usdSum,
        date,
        description,
        qrCode,
        isRegular,
        inStatistics,
        latitude,
        longitude,
        photoUri
    )
