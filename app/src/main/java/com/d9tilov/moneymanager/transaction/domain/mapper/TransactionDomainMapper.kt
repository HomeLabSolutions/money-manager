package com.d9tilov.moneymanager.transaction.domain.mapper

import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import javax.inject.Inject

class TransactionDomainMapper @Inject constructor() {

    fun toDataModel(transaction: Transaction) =
        with(transaction) {
            TransactionDataModel(
                id, type, sum, category.id, currency, date, description, qrCode
            )
        }

    fun toDomain(transactionDataModel: TransactionDataModel, category: Category) =
        with(transactionDataModel) {
            Transaction(id, type, sum, category, currency, date, description, qrCode)
        }
}
