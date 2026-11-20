package com.d9tilov.android.transaction.data.impl

import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.transaction.data.contract.TransactionSource
import com.d9tilov.android.transaction.domain.contract.TransactionRepo
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import com.d9tilov.android.transaction.domain.model.TransactionMinMaxDateModel
import com.d9tilov.android.widget.domain.contract.SpendingWidgetStateObserver
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Inject
class TransactionDataRepo(
    private val transactionSource: TransactionSource,
    private val spendingWidgetStateObserver: SpendingWidgetStateObserver,
) : TransactionRepo {
    override suspend fun addTransaction(transaction: TransactionDataModel) {
        transactionSource.insert(transaction)
        spendingWidgetStateObserver.notifyStateChanged()
    }

    override fun getTransactionById(id: Long): Flow<TransactionDataModel> = transactionSource.getById(id)

    override fun getTransactionsByType(transactionType: TransactionType) =
        transactionSource.getAllByTypePaging(transactionType)

    override fun searchTransactionsByType(
        transactionType: TransactionType,
        query: String,
    ) = transactionSource.searchByTypePaging(transactionType, query)

    override fun getTransactionsByTypeInPeriod(
        from: LocalDateTime,
        to: LocalDateTime,
        transactionType: TransactionType,
        onlyInStatistics: Boolean,
        withRegular: Boolean,
    ): Flow<List<TransactionDataModel>> =
        transactionSource.getAllByTypeInPeriod(
            from,
            to,
            transactionType,
            onlyInStatistics,
            withRegular,
        )

    override suspend fun getAllByCategory(category: Category): List<TransactionDataModel> =
        transactionSource.getAllByCategory(category)

    override fun getByCategoryInPeriod(
        category: Category,
        from: LocalDateTime,
        to: LocalDateTime,
        inStatistics: Boolean,
    ): Flow<List<TransactionDataModel>> = transactionSource.getByCategoryInPeriod(category, from, to, inStatistics)

    override suspend fun getTransactionMinMaxDate(): TransactionMinMaxDateModel = transactionSource.getMinMaxDate()

    override suspend fun update(transaction: TransactionDataModel) {
        transactionSource.update(transaction)
        spendingWidgetStateObserver.notifyStateChanged()
    }

    override suspend fun removeTransaction(transaction: TransactionDataModel) {
        transactionSource.remove(transaction)
        spendingWidgetStateObserver.notifyStateChanged()
    }
}
