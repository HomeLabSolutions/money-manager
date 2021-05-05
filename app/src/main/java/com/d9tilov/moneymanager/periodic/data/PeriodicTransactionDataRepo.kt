package com.d9tilov.moneymanager.periodic.data

import com.d9tilov.moneymanager.periodic.data.entity.PeriodicTransactionData
import com.d9tilov.moneymanager.periodic.data.local.PeriodicTransactionSource
import com.d9tilov.moneymanager.periodic.domain.PeriodicTransactionRepo
import com.d9tilov.moneymanager.transaction.TransactionType

class PeriodicTransactionDataRepo(private val periodicTransactionSource: PeriodicTransactionSource) :
    PeriodicTransactionRepo {

    override fun insert(periodicTransactionData: PeriodicTransactionData) =
        periodicTransactionSource.insert(periodicTransactionData)

    override fun getAll(type: TransactionType) = periodicTransactionSource.getAll(type)

    override fun update(periodicTransactionData: PeriodicTransactionData) =
        periodicTransactionSource.update(periodicTransactionData)

    override fun delete(periodicTransactionData: PeriodicTransactionData) =
        periodicTransactionSource.delete(periodicTransactionData)
}
