package com.d9tilov.moneymanager.transaction.domain

import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.domain.mapper.TransactionDomainMapper
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.functions.BiFunction
import timber.log.Timber

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
                Timber.tag(App.TAG).d("model = ${transactionDataModel.categoryId}")
                categoryInteractor.getCategoryById(transactionDataModel.categoryId)
                    .doOnError { Timber.tag(App.TAG).d("erroraa = {${it.message}}") }
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
                        Timber.tag(App.TAG).d("model = $it")
                        Flowable.zip(
                            categoryInteractor.getCategoryById(it.categoryId).toFlowable()
                                .doOnError { Timber.tag(App.TAG).d("error = {${it.message}}") },
                            Flowable.fromCallable { it },
                            BiFunction<Category, TransactionDataModel, Transaction>
                            { category, transactionDataModel ->
                                Timber.tag(App.TAG).d("FUNC111")
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
                            .doOnComplete { Timber.tag(App.TAG).d("complete2") }
                            .doOnError { Timber.tag(App.TAG).d("error2 = {${it.message}}") }
                    }.toList()
            }

    override fun removeTransaction(transaction: Transaction) =
        transactionRepo.removeTransaction(transactionDomainMapper.toDataModel(transaction))
}
