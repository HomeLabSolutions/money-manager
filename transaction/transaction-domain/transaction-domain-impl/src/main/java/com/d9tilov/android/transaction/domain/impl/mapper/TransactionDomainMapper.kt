package com.d9tilov.android.transaction.domain.impl.mapper

import com.d9tilov.android.category.data.model.Category
import com.d9tilov.android.transaction.data.model.TransactionDataModel
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.domain.model.TransactionChartModel
import java.math.BigDecimal

fun TransactionDataModel.toDomainModel(category: Category): Transaction =
    Transaction.EMPTY.copy(
        id = id,
        clientId = clientId,
        type = type,
        category = category,
        currencyCode = currencyCode,
        sum = sum,
        usdSum = usdSum,
        date = date,
        description = description,
        qrCode = qrCode,
        isRegular = isRegular,
        inStatistics = inStatistics,
        latitude = latitude,
        longitude = longitude,
        photoUri = photoUri
    )

fun TransactionDataModel.toChartModel(
    category: Category,
    currencyCode: String,
    sum: BigDecimal
): TransactionChartModel =
    TransactionChartModel(
        clientId,
        type,
        category,
        currencyCode,
        sum,
        BigDecimal.ZERO
    )
