package com.d9tilov.android.transaction.data.impl.mapper

import com.d9tilov.android.core.model.toType
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.database.entity.TransactionDbModel
import com.d9tilov.android.database.entity.TransactionMinMaxDateDbModel
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import com.d9tilov.android.transaction.domain.model.TransactionMinMaxDateModel

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
        photoUri,
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
        photoUri,
    )

fun TransactionMinMaxDateDbModel.toDataModel(): TransactionMinMaxDateModel =
    TransactionMinMaxDateModel(minDate ?: currentDateTime(), maxDate ?: currentDateTime())
