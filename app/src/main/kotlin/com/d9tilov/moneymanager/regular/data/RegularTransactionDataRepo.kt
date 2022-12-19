package com.d9tilov.moneymanager.regular.data

import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.regular.data.entity.RegularTransactionData
import com.d9tilov.moneymanager.regular.data.local.RegularTransactionSource
import com.d9tilov.moneymanager.regular.domain.RegularTransactionRepo
import com.d9tilov.android.database.model.TransactionType

class RegularTransactionDataRepo(private val regularTransactionSource: RegularTransactionSource) :
    RegularTransactionRepo {

    override suspend fun insert(regularTransactionData: RegularTransactionData) =
        regularTransactionSource.insert(regularTransactionData)

    override fun getAll(type: TransactionType) = regularTransactionSource.getAll(type)

    override suspend fun getAllByCategory(category: Category): List<RegularTransactionData> =
        regularTransactionSource.getAllByCategory(category)

    override suspend fun getById(id: Long): RegularTransactionData = regularTransactionSource.getById(id)

    override suspend fun update(regularTransactionData: RegularTransactionData) =
        regularTransactionSource.update(regularTransactionData)

    override suspend fun delete(regularTransactionData: RegularTransactionData) =
        regularTransactionSource.delete(regularTransactionData)
}
