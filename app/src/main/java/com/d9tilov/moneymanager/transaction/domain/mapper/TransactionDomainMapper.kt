package com.d9tilov.moneymanager.transaction.domain.mapper

import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import javax.inject.Inject

class TransactionDomainMapper @Inject constructor() {

    fun toDataModel(transaction: Transaction) =
        with(transaction) {
            TransactionDataModel(
                id, clientId, type, sum, category.id, currencyCode, date, description, qrCode
            )
        }

    fun toDomain(transactionDataModel: TransactionDataModel, category: Category) =
        with(transactionDataModel) {
            Transaction(id, clientId, type, sum, category, currency, date, description, qrCode)
        }
}
