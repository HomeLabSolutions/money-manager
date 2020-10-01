package com.d9tilov.moneymanager.fixed.domain

import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.exception.CategoryNotFoundException
import com.d9tilov.moneymanager.fixed.domain.entity.FixedTransaction
import com.d9tilov.moneymanager.fixed.domain.mapper.FixedTransactionDomainMapper
import com.d9tilov.moneymanager.transaction.TransactionType
import io.reactivex.Flowable

class FixedTransactionInteractorImpl(
    private val fixedTransactionRepo: FixedTransactionRepo,
    private val categoryInteractor: CategoryInteractor,
    private val fixedTransactionDomainMapper: FixedTransactionDomainMapper
) :
    FixedTransactionInteractor {

    override fun insert(fixedTransactionData: FixedTransaction) =
        fixedTransactionRepo.insert(fixedTransactionDomainMapper.toData(fixedTransactionData))

    override fun getAll(type: TransactionType): Flowable<List<FixedTransaction>> {
        return categoryInteractor.getGroupedCategoriesByType(type)
            .switchMap { categoryList ->
                fixedTransactionRepo.getAll(type)
                    .map { list ->
                        val newList = mutableListOf<FixedTransaction>()
                        for (item in list) {
                            val category =
                                categoryList.find { item.categoryId == it.id }
                                    ?: throw CategoryNotFoundException("Not found category with id: ${item.categoryId}")
                            val fixedTransaction =
                                fixedTransactionDomainMapper.toDomain(item, category)
                            newList.add(fixedTransaction)
                        }
                        newList
                    }
            }
    }

    override fun update(fixedTransactionData: FixedTransaction) =
        fixedTransactionRepo.update(fixedTransactionDomainMapper.toData(fixedTransactionData))

    override fun delete(fixedTransactionData: FixedTransaction) =
        fixedTransactionRepo.delete(fixedTransactionDomainMapper.toData(fixedTransactionData))
}
