package com.d9tilov.moneymanager.transaction.data

import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.data.local.TransactionSource
import com.d9tilov.moneymanager.transaction.domain.TransactionRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

class TransactionDataRepo(private val transactionSource: TransactionSource) : TransactionRepo {

    override suspend fun addTransaction(transaction: TransactionDataModel) {
        transactionSource.insert(transaction)
    }

    override fun getTransactionById(id: Long): Flow<TransactionDataModel> =
        transactionSource.getById(id)

    override fun getTransactionsByType(
        from: LocalDateTime,
        to: LocalDateTime,
        transactionType: TransactionType
    ) = transactionSource.getAllByType(from, to, transactionType)

    override fun getTransactionsByTypeWithoutDate(
        from: LocalDateTime,
        to: LocalDateTime,
        transactionType: TransactionType,
        onlyInStatistics: Boolean
    ): Flow<List<TransactionDataModel>> =
        transactionSource.getAllByTypeWithoutDates(from, to, transactionType, onlyInStatistics)

    override fun getByCategory(category: Category): Flow<List<TransactionDataModel>> =
        transactionSource.getByCategory(category)

    override suspend fun getCountByCurrencyCode(code: String): Int =
        transactionSource.getCountByCurrencyCode(code)

    override suspend fun update(transaction: TransactionDataModel) =
        transactionSource.update(transaction)

    override suspend fun removeTransaction(transaction: TransactionDataModel) {
        transactionSource.remove(transaction)
    }

    override fun removeAllByCategory(category: Category) = transactionSource.removeAllByCategory(category)

    override suspend fun clearAll() {
        transactionSource.clearAll()
    }
}
