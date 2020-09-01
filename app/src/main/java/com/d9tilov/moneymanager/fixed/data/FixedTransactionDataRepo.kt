package com.d9tilov.moneymanager.fixed.data

import com.d9tilov.moneymanager.fixed.data.entity.FixedTransactionData
import com.d9tilov.moneymanager.fixed.data.local.FixedTransactionSource
import com.d9tilov.moneymanager.fixed.domain.FixedTransactionRepo

class FixedTransactionDataRepo(private val fixedTransactionSource: FixedTransactionSource) : FixedTransactionRepo {

    override fun insert(fixedTransactionData: FixedTransactionData) = fixedTransactionSource.insert(fixedTransactionData)

    override fun update(fixedTransactionData: FixedTransactionData) = fixedTransactionSource.update(fixedTransactionData)

    override fun delete(fixedTransactionData: FixedTransactionData) = fixedTransactionSource.delete(fixedTransactionData)
}
