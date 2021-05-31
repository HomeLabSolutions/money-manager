package com.d9tilov.moneymanager.transaction.domain.mapper

import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction

fun Transaction.toDataModel(): TransactionDataModel = TransactionDataModel(
    id, clientId, type, sum, category.id, currencyCode, date, description, qrCode, isRegular
)

fun TransactionDataModel.toDomainModel(category: Category): Transaction =
    Transaction(id, clientId, type, sum, category, currency, date, description, qrCode, isRegular)
