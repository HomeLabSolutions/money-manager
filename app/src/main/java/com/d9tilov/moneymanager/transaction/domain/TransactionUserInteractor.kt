package com.d9tilov.moneymanager.transaction.domain

import androidx.paging.PagedList
import androidx.paging.toFlowable
import com.d9tilov.moneymanager.category.CategoryType
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.exception.CategoryNotFoundException
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDateDataModel
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.domain.mapper.TransactionDomainMapper
import com.d9tilov.moneymanager.transaction.domain.mapper.TransactionHeaderDomainMapper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

class TransactionUserInteractor(
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
            TransactionType.INCOME -> CategoryType.INCOME
            TransactionType.EXPENSE -> CategoryType.EXPENSE
        }
        return categoryInteractor.getAllCategoriesByType(categoryType)
            .flatMap { categoryList ->
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
                    .toFlowable(
                        PagedList.Config.Builder()
                            .setInitialLoadSizeHint(INITIAL_PAGE_SIZE)
                            .setPageSize(PAGE_SIZE)
                            .setEnablePlaceholders(false)
                            .build()
                    )
            }
    }

    override fun removeTransaction(transaction: Transaction): Completable {
        return transactionRepo.removeTransaction(transactionDomainMapper.toDataModel(transaction))
    }

    override fun clearAll(): Completable {
        return transactionRepo.clearAll()
    }

    companion object {
        const val PAGE_SIZE = 30
        const val INITIAL_PAGE_SIZE = 3 * PAGE_SIZE
    }
}
