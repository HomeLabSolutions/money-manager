package com.d9tilov.android.transaction.domain.impl.mapper

import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.utils.reduceScale
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.domain.model.TransactionChartModel
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import java.math.BigDecimal

fun Transaction.toDataModel(): TransactionDataModel =
    TransactionDataModel(
        id,
        clientId,
        type,
        category.id,
        currencyCode,
        sum,
        usdSum,
        date,
        description,
        qrCode,
        isRegular,
        inStatistics,
        locationData,
        photoUri,
    )

fun TransactionDataModel.toDomainModel(category: Category): Transaction =
    Transaction.EMPTY.copy(
        id = id,
        clientId = clientId,
        type = type,
        category = category,
        currencyCode = currencyCode,
        sum = sum.reduceScale(),
        usdSum = usdSum.reduceScale(),
        date = date,
        description = description,
        qrCode = qrCode,
        isRegular = isRegular,
        inStatistics = inStatistics,
        locationData = location,
        photoUri = photoUri,
    )

fun TransactionDataModel.toChartModel(
    category: Category,
    currencyCode: String,
    sum: BigDecimal,
): TransactionChartModel =
    TransactionChartModel(
        clientId,
        type,
        category,
        currencyCode,
        sum,
        BigDecimal.ZERO,
    )
