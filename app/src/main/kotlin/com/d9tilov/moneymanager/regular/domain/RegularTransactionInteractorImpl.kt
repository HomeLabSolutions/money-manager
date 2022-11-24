package com.d9tilov.moneymanager.regular.domain

import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.exception.CategoryException
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.regular.domain.mapper.toData
import com.d9tilov.moneymanager.regular.domain.mapper.toDomain
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
import com.d9tilov.moneymanager.user.domain.UserInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class RegularTransactionInteractorImpl(
    private val regularTransactionRepo: RegularTransactionRepo,
    private val categoryInteractor: CategoryInteractor,
    private val userInteractor: UserInteractor
) : RegularTransactionInteractor {

    override fun createDefault(): Flow<RegularTransaction> = userInteractor.getCurrentCurrencyFlow()
        .map { RegularTransaction.EMPTY.copy(currencyCode = it.code) }

    override suspend fun insert(regularTransactionData: RegularTransaction) {
        regularTransactionRepo.insert(regularTransactionData.toData())
    }

    override fun getAll(type: TransactionType): Flow<List<RegularTransaction>> {
        return categoryInteractor.getGroupedCategoriesByType(type)
            .flatMapLatest { categoryList ->
                regularTransactionRepo.getAll(type).map { list ->
                    val newList = mutableListOf<RegularTransaction>()
                    for (item in list) {
                        val category =
                            categoryList.find { item.categoryId == it.id }
                                ?: throw CategoryException.CategoryNotFoundException("getAll Not found category with id: ${item.categoryId}")
                        val regularTransaction = item.toDomain(category)
                        newList.add(regularTransaction)
                    }
                    newList
                }
            }
    }

    override suspend fun removeAllByCategory(category: Category) {
        regularTransactionRepo.getAllByCategory(category)
            .map { tr -> delete(tr.toDomain(category)) }
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
