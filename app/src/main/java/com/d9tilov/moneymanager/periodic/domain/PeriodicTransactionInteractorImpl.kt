package com.d9tilov.moneymanager.periodic.domain

import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.exception.CategoryNotFoundException
import com.d9tilov.moneymanager.periodic.domain.entity.PeriodicTransaction
import com.d9tilov.moneymanager.periodic.domain.mapper.PeriodicTransactionDomainMapper
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.user.domain.UserInteractor
import io.reactivex.Flowable

class PeriodicTransactionInteractorImpl(
    private val periodicTransactionRepo: PeriodicTransactionRepo,
    private val categoryInteractor: CategoryInteractor,
    private val userInteractor: UserInteractor,
    private val periodicTransactionDomainMapper: PeriodicTransactionDomainMapper
) :
    PeriodicTransactionInteractor {

    override fun insert(periodicTransactionData: PeriodicTransaction) =
        userInteractor.getCurrentUser().firstOrError().flatMapCompletable {
            periodicTransactionRepo.insert(
                periodicTransactionDomainMapper.toData(
                    periodicTransactionData.copy(currencyCode = it.currencyCode)
                )
            )
        }

    override fun getAll(type: TransactionType): Flowable<List<PeriodicTransaction>> {
        return categoryInteractor.getGroupedCategoriesByType(type)
            .switchMap { categoryList ->
                periodicTransactionRepo.getAll(type)
                    .map { list ->
                        val newList = mutableListOf<PeriodicTransaction>()
                        for (item in list) {
                            val category =
                                categoryList.find { item.categoryId == it.id }
                                    ?: throw CategoryNotFoundException("Not found category with id: ${item.categoryId}")
                            val fixedTransaction =
                                periodicTransactionDomainMapper.toDomain(item, category)
                            newList.add(fixedTransaction)
                        }
                        newList
                    }
            }
    }

    override fun update(periodicTransactionData: PeriodicTransaction) =
        periodicTransactionRepo.update(
            periodicTransactionDomainMapper.toData(
                periodicTransactionData
            )
        )

    override fun delete(periodicTransactionData: PeriodicTransaction) =
        periodicTransactionRepo.delete(
            periodicTransactionDomainMapper.toData(
                periodicTransactionData
            )
        )
}
