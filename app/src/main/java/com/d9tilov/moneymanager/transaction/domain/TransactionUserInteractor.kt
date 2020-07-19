package com.d9tilov.moneymanager.transaction.domain

import android.util.Log
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
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionHeader
import com.d9tilov.moneymanager.transaction.domain.mapper.TransactionDomainMapper
import com.d9tilov.moneymanager.transaction.domain.mapper.TransactionHeaderDomainMapper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

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
        var itemPosition = -1
        var itemHeaderPosition = itemPosition
        return categoryInteractor.getAllCategoriesByType(categoryType)
            .flatMap { categoryList ->
                transactionRepo.getTransactionsByType(transactionType = type)
                    .map { transactionBaseDataModel ->
                        if (transactionBaseDataModel is TransactionDateDataModel) {
                            itemPosition++
                            itemHeaderPosition = itemPosition
                            return@map transactionHeaderDomainMapper.toDomain(
                                transactionBaseDataModel,
                                itemHeaderPosition
                            )
                        }
                        if (transactionBaseDataModel is TransactionDataModel) {
                            val category =
                                categoryList.find { transactionBaseDataModel.categoryId == it.id }
                            if (category == null) {
                                throw CategoryNotFoundException("Not found category with id: ${transactionBaseDataModel.categoryId}")
                            } else {
                                itemPosition++
                                return@map transactionDomainMapper.toDomain(
                                    transactionBaseDataModel,
                                    category,
                                    itemHeaderPosition
                                )
                            }
                        }
                        throw IllegalStateException("Unknown TransactionDataItem implementation")
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

    override fun getTransactionsByType2(type: TransactionType): Single<List<BaseTransaction>> {
        val categoryType = when (type) {
            TransactionType.INCOME -> CategoryType.INCOME
            TransactionType.EXPENSE -> CategoryType.EXPENSE
        }
        var itemPosition = 0
        return categoryInteractor.getAllCategoriesByType(categoryType)
            .firstOrError()
            .flatMap { categoryList ->
                transactionRepo.getAllByType2(type)
                    .flatMap {
                        Observable.fromIterable(it)
                            .map { transactionBaseDataModel ->
                                if (transactionBaseDataModel is TransactionDateDataModel) {
                                    return@map transactionHeaderDomainMapper.toDomain(
                                        transactionBaseDataModel,
                                        itemPosition
                                    )
                                }
                                if (transactionBaseDataModel is TransactionDataModel) {
                                    val category =
                                        categoryList.find { transactionBaseDataModel.categoryId == it.id }
                                    if (category == null) {
                                        throw CategoryNotFoundException("Not found category with id: ${transactionBaseDataModel.categoryId}")
                                    } else {
                                        return@map transactionDomainMapper.toDomain(
                                            transactionBaseDataModel,
                                            category,
                                            itemPosition
                                        )
                                    }
                                }
                                ++itemPosition
                                throw IllegalStateException("Unknown TransactionDataItem implementation")

                            }.toList()
                    }
            }
    }

    override fun removeTransaction(transaction: BaseTransaction): Completable {
        return if (transaction is Transaction) {
            transactionRepo.removeTransaction(transactionDomainMapper.toDataModel(transaction))
        } else {
            transactionRepo.removeTransaction(transactionHeaderDomainMapper.toDataModel(transaction as TransactionHeader))
        }
    }

    companion object {
        private const val PAGE_SIZE = 30
        private const val INITIAL_PAGE_SIZE = 3 * PAGE_SIZE
    }
}
