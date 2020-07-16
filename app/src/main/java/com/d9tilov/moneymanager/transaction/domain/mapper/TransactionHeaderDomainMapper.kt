package com.d9tilov.moneymanager.transaction.domain.mapper

import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDateDataModel
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionHeader
import javax.inject.Inject

class TransactionHeaderDomainMapper @Inject constructor() {

    fun toDataModel(transaction: TransactionHeader) =
        with(transaction) { TransactionDateDataModel("", TransactionType.EXPENSE, date) }

    fun toDomain(transactionDateDataModel: TransactionDateDataModel, position:Int) =
        with(transactionDateDataModel) {
            TransactionHeader(date, position)
        }
}
