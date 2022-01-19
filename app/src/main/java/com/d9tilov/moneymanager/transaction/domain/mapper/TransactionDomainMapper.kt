package com.d9tilov.moneymanager.transaction.domain.mapper

import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction

fun Transaction.toDataModel(): TransactionDataModel = TransactionDataModel(
    id, clientId, type, category.id, currencyCode, sum, usdSum, date, description, qrCode, isRegular, inStatistics
)

fun TransactionDataModel.toDomainModel(category: Category): Transaction =
    Transaction(id, clientId, type, category, currency, sum, usdSum, date, description, qrCode, isRegular, inStatistics)
