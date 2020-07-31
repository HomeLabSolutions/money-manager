package com.d9tilov.moneymanager.transaction.data

import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.data.local.TransactionSource
import com.d9tilov.moneymanager.transaction.domain.TransactionRepo
import io.reactivex.Completable
import io.reactivex.Flowable
import java.util.Date

class TransactionDataRepo(private val transactionSource: TransactionSource) : TransactionRepo {

    override fun addTransaction(transaction: TransactionDataModel) =
        transactionSource.insert(transaction)

    override fun getTransactionById(id: Long): Flowable<TransactionDataModel> =
        transactionSource.getById(id)

    override fun getTransactionsByType(
        from: Date,
        to: Date,
        transactionType: TransactionType
    ) = transactionSource.getAllByType(from, to, transactionType)

    override fun update(transaction: TransactionDataModel) = transactionSource.update(transaction)

    override fun removeTransaction(transaction: TransactionDataModel): Completable {
        return transactionSource.remove(transaction)
    }

    override fun clearAll(): Completable {
        return transactionSource.clearAll()
    }
}
