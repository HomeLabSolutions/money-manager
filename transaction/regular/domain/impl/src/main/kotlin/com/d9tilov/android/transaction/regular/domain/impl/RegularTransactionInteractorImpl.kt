package com.d9tilov.android.transaction.regular.domain.impl

import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.category.domain.entity.exception.CategoryException
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionRepo
import com.d9tilov.android.transaction.regular.domain.impl.mapper.toData
import com.d9tilov.android.transaction.regular.domain.impl.mapper.toDomain
import com.d9tilov.android.transaction.regular.domain.model.RegularTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class RegularTransactionInteractorImpl(
    private val currencyInteractor: CurrencyInteractor,
    private val regularTransactionRepo: RegularTransactionRepo,
    private val categoryInteractor: CategoryInteractor,
) : RegularTransactionInteractor {
    override fun createDefault(): Flow<RegularTransaction> =
        currencyInteractor
            .getMainCurrencyFlow()
            .map { RegularTransaction.EMPTY.copy(currencyCode = it.code) }

    override suspend fun insert(regularTransactionData: RegularTransaction) {
        regularTransactionRepo.insert(regularTransactionData.toData())
    }

    override fun getAll(type: TransactionType): Flow<List<RegularTransaction>> =
        categoryInteractor
            .getGroupedCategoriesByType(type)
            .flatMapLatest { categoryList ->
                regularTransactionRepo.getAll(type).map { list ->
                    val newList = mutableListOf<RegularTransaction>()
                    for (item in list) {
                        val category =
                            categoryList.find { item.categoryId == it.id }
                                ?: throw CategoryException
                                    .CategoryNotFoundException(
                                        "getAll Not found category with id: ${item.categoryId}",
                                    )
                        val regularTransaction = item.toDomain(category)
                        newList.add(regularTransaction)
                    }
                    newList
                }
            }

    override suspend fun removeAllByCategory(category: Category) {
        regularTransactionRepo
            .getAllByCategory(category)
            .forEach { tr -> delete(tr.toDomain(category)) }
    }

    override suspend fun getById(id: Long): RegularTransaction {
        val categoryId = regularTransactionRepo.getById(id).categoryId
        val category = categoryInteractor.getCategoryById(categoryId)
        return regularTransactionRepo.getById(id).toDomain(category)
    }

    override suspend fun update(regularTransactionData: RegularTransaction) {
        regularTransactionRepo.update(regularTransactionData.toData())
    }

    override suspend fun delete(regularTransactionData: RegularTransaction) {
        regularTransactionRepo.delete(regularTransactionData.toData())
    }
}
