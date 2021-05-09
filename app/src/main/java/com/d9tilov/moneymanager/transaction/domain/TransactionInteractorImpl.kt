package com.d9tilov.moneymanager.transaction.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.exception.CategoryNotFoundException
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDateDataModel
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.domain.mapper.TransactionDomainMapper
import com.d9tilov.moneymanager.transaction.domain.mapper.TransactionHeaderDomainMapper
import com.d9tilov.moneymanager.user.domain.UserInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map

class TransactionInteractorImpl(
    private val transactionRepo: TransactionRepo,
    private val categoryInteractor: CategoryInteractor,
    private val userInteractor: UserInteractor,
    private val transactionDomainMapper: TransactionDomainMapper,
    private val transactionHeaderDomainMapper: TransactionHeaderDomainMapper
) : TransactionInteractor {

    override suspend fun addTransaction(transaction: Transaction) {
        val currencyCode = userInteractor.getCurrency()
        transactionRepo.addTransaction(
            transactionDomainMapper.toDataModel(
                transaction.copy(currencyCode = currencyCode)
            )
        )
        val category = categoryInteractor.getCategoryById(transaction.category.id)
        val count = category.usageCount + 1
        categoryInteractor.update(category.copy(usageCount = count))
    }

    override fun getTransactionById(id: Long): Flow<Transaction> {
        return transactionRepo.getTransactionById(id)
            .map { transactionDataModel ->
                val category = categoryInteractor.getCategoryById(transactionDataModel.categoryId)
                Transaction(
                    transactionDataModel.id,
                    transactionDataModel.clientId,
                    transactionDataModel.type,
                    transactionDataModel.sum,
                    category,
                    transactionDataModel.currency,
                    transactionDataModel.date,
                    transactionDataModel.description,
                    transactionDataModel.qrCode
                )
            }
    }

    override fun getTransactionsByType(type: TransactionType): Flow<PagingData<BaseTransaction>> {
        return categoryInteractor.getGroupedCategoriesByType(type)
            .flatMapMerge { categoryList ->
                transactionRepo.getTransactionsByType(transactionType = type)
                    .map {
                        it.map { item ->
                            if (item is TransactionDateDataModel) {
                                transactionHeaderDomainMapper.toDomain(item)
                            }
                            if (item is TransactionDataModel) {
                                val category =
                                    categoryList.find { item.categoryId == it.id }
                                        ?: throw CategoryNotFoundException("Not found category with id: ${item.categoryId}")
                                transactionDomainMapper.toDomain(item, category)
                            } else {
                                throw IllegalStateException("Unknown TransactionDataItem implementation: $item")
                            }
                        }
                    }
            }
    }

    override suspend fun update(transaction: Transaction) =
        transactionRepo.update(transactionDomainMapper.toDataModel(transaction))

    override suspend fun removeTransaction(transaction: Transaction) {
        transactionRepo.removeTransaction(transactionDomainMapper.toDataModel(transaction))
    }

    override suspend fun removeAllByCategory(category: Category) {
        transactionRepo.removeAllByCategory(category)
    }

    override suspend fun clearAll() {
        transactionRepo.clearAll()
    }
}
