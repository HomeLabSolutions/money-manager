package com.d9tilov.moneymanager.transaction.data

import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.data.local.TransactionSource
import com.d9tilov.moneymanager.transaction.domain.TransactionRepo
import kotlinx.coroutines.flow.Flow
import java.util.Date

class TransactionDataRepo(private val transactionSource: TransactionSource) : TransactionRepo {

    override suspend fun addTransaction(transaction: TransactionDataModel) {
        transactionSource.insert(transaction)
    }

    override fun getTransactionById(id: Long): Flow<TransactionDataModel> =
        transactionSource.getById(id)

    override fun getTransactionsByType(
        from: Date,
        to: Date,
        transactionType: TransactionType
    ) = transactionSource.getAllByType(from, to, transactionType)

    override suspend fun update(transaction: TransactionDataModel) =
        transactionSource.update(transaction)

    override suspend fun removeTransaction(transaction: TransactionDataModel) {
        transactionSource.remove(transaction)
    }

    override suspend fun removeAllByCategory(category: Category) {
        transactionSource.removeAllByCategory(category)
    }

    override suspend fun clearAll() {
        transactionSource.clearAll()
    }
}
