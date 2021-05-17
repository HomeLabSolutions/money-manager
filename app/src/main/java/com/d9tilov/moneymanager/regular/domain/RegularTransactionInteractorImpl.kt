package com.d9tilov.moneymanager.regular.domain

import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.exception.CategoryNotFoundException
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.regular.domain.mapper.RegularTransactionDomainMapper
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.user.domain.UserInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

class RegularTransactionInteractorImpl(
    private val regularTransactionRepo: RegularTransactionRepo,
    private val categoryInteractor: CategoryInteractor,
    private val userInteractor: UserInteractor,
    private val regularTransactionDomainMapper: RegularTransactionDomainMapper
) : RegularTransactionInteractor {

    override suspend fun insert(regularTransactionData: RegularTransaction) {
        val currency = userInteractor.getCurrency()
        regularTransactionRepo.insert(
            regularTransactionDomainMapper.toData(
                regularTransactionData.copy(currencyCode = currency)
            )
        )
    }

    override fun getAll(type: TransactionType): Flow<List<RegularTransaction>> {
        return categoryInteractor.getGroupedCategoriesByType(type)
            .flatMapConcat { categoryList ->
                regularTransactionRepo.getAll(type).map { list ->
                    val newList = mutableListOf<RegularTransaction>()
                    for (item in list) {
                        val category =
                            categoryList.find { item.categoryId == it.id }
                                ?: throw CategoryNotFoundException("Not found category with id: ${item.categoryId}")
                        val regularTransaction =
                            regularTransactionDomainMapper.toDomain(item, category)
                        newList.add(regularTransaction)
                    }
                    newList
                }
            }
    }

    override suspend fun update(regularTransactionData: RegularTransaction) {
        regularTransactionRepo.update(
            regularTransactionDomainMapper.toData(
                regularTransactionData
            )
        )
    }

    override suspend fun delete(regularTransactionData: RegularTransaction) {
        regularTransactionRepo.delete(
            regularTransactionDomainMapper.toData(
                regularTransactionData
            )
        )
    }
}
