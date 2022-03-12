package com.d9tilov.moneymanager.transaction.domain.mapper

import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionChartModel

fun Transaction.toDataModel(): TransactionDataModel = TransactionDataModel(
    id, clientId, type, category.id, currencyCode, sum, usdSum, date, description, qrCode, isRegular, inStatistics
)

fun TransactionDataModel.toDomainModel(category: Category): Transaction =
    Transaction(id, clientId, type, category, currencyCode, sum, usdSum, date, description, qrCode, isRegular, inStatistics)

fun TransactionDataModel.toChartModel(category: Category): TransactionChartModel =
    TransactionChartModel(clientId, type, category, currencyCode, sum, usdSum, inStatistics)
