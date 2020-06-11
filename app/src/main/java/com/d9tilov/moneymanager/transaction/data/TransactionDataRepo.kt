package com.d9tilov.moneymanager.transaction.data

import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.data.local.TransactionSource
import com.d9tilov.moneymanager.transaction.domain.TransactionRepo
import io.reactivex.Flowable

class TransactionDataRepo(private val transactionSource: TransactionSource) : TransactionRepo {

    override fun addTransaction(transaction: TransactionDataModel) =
        transactionSource.insert(transaction)

    override fun getTransactionById(id: Long): Flowable<TransactionDataModel> =
        transactionSource.getById(id)

    override fun getTransactionsByType(transactionType: TransactionType) =
        transactionSource.getByType(transactionType)

    override fun removeTransaction(transaction: TransactionDataModel) =
        transactionSource.remove(transaction)
}
