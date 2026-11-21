package com.d9tilov.android.transaction.domain.impl.mapper

import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.utils.reduceScale
import com.d9tilov.android.core.utils.toDecimal
import com.d9tilov.android.core.utils.toMoney
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
        sum.currency.name,
        sum.toDecimal(),
        usdSum,
        date,
        description,
        qrCode,
        isRegular,
        inStatistics,
        place,
        photoUri,
    )

fun TransactionDataModel.toDomainModel(
    category: Category,
    roundUsd: Boolean = true,
): Transaction =
    Transaction.EMPTY.copy(
        id = id,
        clientId = clientId,
        type = type,
        category = category,
        sum = sum.toMoney(currencyCode),
        usdSum = if (roundUsd) usdSum.reduceScale() else usdSum,
        date = date,
        description = description,
        qrCode = qrCode,
        isRegular = isRegular,
        inStatistics = inStatistics,
        place = place,
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
