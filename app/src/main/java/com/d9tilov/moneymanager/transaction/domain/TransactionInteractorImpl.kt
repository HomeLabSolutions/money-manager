package com.d9tilov.moneymanager.transaction.domain

import androidx.paging.PagedList
import androidx.paging.toFlowable
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.exception.CategoryNotFoundException
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDateDataModel
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.domain.mapper.TransactionDomainMapper
import com.d9tilov.moneymanager.transaction.domain.mapper.TransactionHeaderDomainMapper
import com.d9tilov.moneymanager.transaction.domain.paging.PagingConfig
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

class TransactionInteractorImpl(
    private val transactionRepo: TransactionRepo,
    private val categoryInteractor: CategoryInteractor,
    private val transactionDomainMapper: TransactionDomainMapper,
    private val transactionHeaderDomainMapper: TransactionHeaderDomainMapper
) : TransactionInteractor {

    override fun addTransaction(transaction: Transaction) =
        transactionRepo.addTransaction(transactionDomainMapper.toDataModel(transaction))

    override fun getTransactionById(id: Long): Flowable<Transaction> {
        return transactionRepo.getTransactionById(id)
            .flatMapMaybe { transactionDataModel ->
                categoryInteractor.getCategoryById(transactionDataModel.categoryId)
                    .flatMap {
                        Maybe.fromCallable {
                            Transaction(
                                transactionDataModel.id,
                                transactionDataModel.clientId,
                                transactionDataModel.type,
                                transactionDataModel.sum,
                                it,
                                transactionDataModel.currency,
                                transactionDataModel.date,
                                transactionDataModel.description,
                                transactionDataModel.qrCode
                            )
                        }
                    }
            }
    }

    override fun getTransactionsByType(type: TransactionType): Flowable<PagedList<BaseTransaction>> {
        val categoryType = when (type) {
            TransactionType.INCOME -> TransactionType.INCOME
            TransactionType.EXPENSE -> TransactionType.EXPENSE
        }
        return categoryInteractor.getGroupedCategoriesByType(categoryType)
            .switchMap { categoryList ->
                transactionRepo.getTransactionsByType(transactionType = type)
                    .map { item ->
                        if (item is TransactionDateDataModel) {
                            return@map transactionHeaderDomainMapper.toDomain(item)
                        }
                        if (item is TransactionDataModel) {
                            val category =
                                categoryList.find { item.categoryId == it.id }
                                    ?: throw CategoryNotFoundException("Not found category with id: ${item.categoryId}")
                            transactionDomainMapper.toDomain(item, category)
                        } else {
                            throw IllegalStateException("Unknown TransactionDataItem implementation")
                        }
                    }
                    .toFlowable(PagingConfig.createConfig())
            }
    }

    override fun update(transaction: Transaction) =
        transactionRepo.update(transactionDomainMapper.toDataModel(transaction))

    override fun removeTransaction(transaction: Transaction): Completable {
        return transactionRepo.removeTransaction(transactionDomainMapper.toDataModel(transaction))
    }

    override fun removeAllByCategory(category: Category): Completable {
        return transactionRepo.removeAllByCategory(category)
    }

    override fun clearAll(): Completable {
        return transactionRepo.clearAll()
    }
}
