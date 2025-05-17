package com.d9tilov.android.transaction.regular.data.impl

import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.transaction.regular.data.contract.RegularTransactionSource
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionRepo
import com.d9tilov.android.transaction.regular.domain.model.RegularTransactionData
import javax.inject.Inject

class RegularTransactionDataRepo @Inject constructor(
    private val regularTransactionSource: RegularTransactionSource,
) : RegularTransactionRepo {
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
