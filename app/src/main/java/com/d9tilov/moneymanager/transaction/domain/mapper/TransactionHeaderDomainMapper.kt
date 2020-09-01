package com.d9tilov.moneymanager.transaction.domain.mapper

import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDateDataModel
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionHeader
import javax.inject.Inject

class TransactionHeaderDomainMapper @Inject constructor() {

    fun toDataModel(transaction: TransactionHeader) =
        with(transaction) {
            TransactionDateDataModel(
                type = TransactionType.EXPENSE,
                date = date,
                currency = currency
            )
        }

    fun toDomain(transactionDateDataModel: TransactionDateDataModel) =
        with(transactionDateDataModel) {
            TransactionHeader(date, currency)
        }
}
