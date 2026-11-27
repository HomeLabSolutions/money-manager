package com.d9tilov.android.transaction.domain.impl.mapper

import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.Currency
import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.domain.model.TransactionChartModel
import com.d9tilov.android.transaction.domain.model.TransactionDataModel

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
): Transaction {
    val currency = checkNotNull(Currency.fromCode(currencyCode)) { "Unsupported currency: $currencyCode" }
    return Transaction.EMPTY.copy(
        id = id,
        clientId = clientId,
        type = type,
        category = category,
        sum = sum.toMoney(currency),
        usdSum = if (roundUsd) usdSum.roundTo(Currency.USD.precision) else usdSum,
        date = date,
        description = description,
        qrCode = qrCode,
        isRegular = isRegular,
        inStatistics = inStatistics,
        place = place,
        photoUri = photoUri,
    )
}

fun TransactionDataModel.toChartModel(
    category: Category,
    currencyCode: String,
    sum: Decimal,
): TransactionChartModel =
    TransactionChartModel(
        clientId,
        type,
        category,
        currencyCode,
        sum,
        Decimal.ZERO,
    )
