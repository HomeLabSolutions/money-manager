package com.d9tilov.moneymanager.transaction.data

import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.data.local.TransactionSource
import com.d9tilov.moneymanager.transaction.domain.TransactionRepo
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
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
    ) = transactionSource.getAllByTypePaging(from, to, transactionType)

    override fun getTransactionsByType2(
        from: LocalDateTime,
        to: LocalDateTime,
        transactionType: TransactionType
    ) = transactionSource.getAllByTypeInPeriod(
        from = from,
        to = to,
        transactionType = transactionType,
        onlyInStatistics = false,
        withRegular = true
    )

    override fun getTransactionsByTypeInPeriod(
        from: LocalDateTime,
        to: LocalDateTime,
        transactionType: TransactionType,
        onlyInStatistics: Boolean,
        withRegular: Boolean
    ): Flow<List<TransactionDataModel>> =
        transactionSource.getAllByTypeInPeriod(from, to, transactionType, onlyInStatistics, withRegular)

    override suspend fun getAllByCategory(category: Category): List<TransactionDataModel> =
        transactionSource.getAllByCategory(category)

    override fun getByCategoryInPeriod(
        category: Category,
        from: LocalDateTime,
        to: LocalDateTime,
        inStatistics: Boolean
    ): Flow<List<TransactionDataModel>> = transactionSource.getByCategoryInPeriod(category, from, to, inStatistics)

    override suspend fun getCountByCurrencyCode(code: String) =
        transactionSource.getCountByCurrencyCode(code)

    override suspend fun update(transaction: TransactionDataModel) =
        transactionSource.update(transaction)

    override suspend fun removeTransaction(transaction: TransactionDataModel) {
        transactionSource.remove(transaction)
    }

    override suspend fun clearAll() {
        transactionSource.clearAll()
    }
}
