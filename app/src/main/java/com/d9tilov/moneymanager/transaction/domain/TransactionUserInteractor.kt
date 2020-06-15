package com.d9tilov.moneymanager.transaction.domain

import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.domain.mapper.TransactionDomainMapper
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.functions.BiFunction

class TransactionUserInteractor(
    private val transactionRepo: TransactionRepo,
    private val categoryInteractor: CategoryInteractor,
    private val transactionDomainMapper: TransactionDomainMapper
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

    override fun getTransactionsByType(type: TransactionType): Flowable<List<Transaction>> =
        transactionRepo.getTransactionsByType(type)
            .flatMapSingle { list ->
                Flowable.fromIterable(list)
                    .flatMap {
                        Flowable.zip(
                            categoryInteractor.getCategoryById(it.categoryId).toFlowable(),
                            Flowable.fromCallable { it },
                            BiFunction<Category, TransactionDataModel, Transaction>
                            { category, transactionDataModel ->
                                Transaction(
                                    transactionDataModel.id,
                                    transactionDataModel.type,
                                    transactionDataModel.sum,
                                    category,
                                    transactionDataModel.currency,
                                    transactionDataModel.date,
                                    transactionDataModel.description,
                                    transactionDataModel.qrCode
                                )
                            }
                        )
                    }.toList()
            }

    override fun removeTransaction(transaction: Transaction) =
        transactionRepo.removeTransaction(transactionDomainMapper.toDataModel(transaction))
}
