package com.d9tilov.moneymanager.transaction.domain.mapper

import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDateDataModel
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionHeader

fun TransactionHeader.toDataModel(): TransactionDateDataModel = TransactionDateDataModel(
    type = TransactionType.EXPENSE,
    date = date,
    currency = currency
)

fun TransactionDateDataModel.toDomainModel(): TransactionHeader = TransactionHeader(date, currency)
