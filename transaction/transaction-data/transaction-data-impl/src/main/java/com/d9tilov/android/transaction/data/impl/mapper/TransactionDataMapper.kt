package com.d9tilov.android.transaction.data.impl.mapper

import com.d9tilov.android.core.model.toType
import com.d9tilov.android.database.entity.TransactionDbModel
import com.d9tilov.android.transaction.domain.model.TransactionDataModel

fun TransactionDbModel.toDataModel(): com.d9tilov.android.transaction.domain.model.TransactionDataModel =
    com.d9tilov.android.transaction.domain.model.TransactionDataModel(
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

fun com.d9tilov.android.transaction.domain.model.TransactionDataModel.toDbModel(): TransactionDbModel =
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
