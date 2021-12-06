package com.d9tilov.moneymanager.transaction.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.exception.CategoryNotFoundException
import com.d9tilov.moneymanager.core.util.getStartDateOfFiscalPeriod
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDateDataModel
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.domain.mapper.toDataModel
import com.d9tilov.moneymanager.transaction.domain.mapper.toDomainModel
import com.d9tilov.moneymanager.user.domain.UserInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.util.Date

class TransactionInteractorImpl(
    private val transactionRepo: TransactionRepo,
    private val categoryInteractor: CategoryInteractor,
    private val userInteractor: UserInteractor,
    private val currencyInteractor: CurrencyInteractor
) : TransactionInteractor {

    override suspend fun addTransaction(transaction: Transaction) {
        val currencyCode = userInteractor.getCurrentCurrency()
        val newTransaction = transaction.copy(currencyCode = currencyCode).toDataModel()
        transactionRepo.addTransaction(newTransaction)
        val category = categoryInteractor.getCategoryById(transaction.category.id)
        val count = category.usageCount + 1
        categoryInteractor.update(category.copy(usageCount = count))
        val currency = currencyInteractor.getCurrencyByCode(currencyCode)
        currencyInteractor.updateCurrency(currency.copy(used = true))
    }

    override fun getTransactionById(id: Long): Flow<Transaction> {
        return transactionRepo.getTransactionById(id)
            .map { transactionDataModel ->
                val category = categoryInteractor.getCategoryById(transactionDataModel.categoryId)
                Transaction(
                    transactionDataModel.id,
                    transactionDataModel.clientId,
                    transactionDataModel.type,
                    transactionDataModel.sum,
                    category,
                    transactionDataModel.currency,
                    transactionDataModel.date,
                    transactionDataModel.description,
                    transactionDataModel.qrCode
                )
            }
    }

    override fun getTransactionsByType(type: TransactionType): Flow<PagingData<BaseTransaction>> {
        return categoryInteractor.getGroupedCategoriesByType(type)
            .flatMapConcat { categoryList ->
                transactionRepo.getTransactionsByType(transactionType = type)
                    .map {
                        it.map { item ->
                            when (item) {
                                is TransactionDateDataModel -> {
                                    item.toDomainModel()
                                }
                                is TransactionDataModel -> {
                                    val category =
                                        categoryList.find { listItem -> item.categoryId == listItem.id }
                                            ?: throw CategoryNotFoundException("Not found category with id: ${item.categoryId}")
                                    item.toDomainModel(category)
                                }
                                else -> {
                                    throw IllegalStateException("Unknown TransactionDataItem implementation: $item")
                                }
                            }
                        }
                    }
            }
    }

    override fun getTransactionsByTypeWithoutDates(
        from: Date,
        to: Date,
        type: TransactionType
    ): Flow<List<Transaction>> =
        categoryInteractor.getGroupedCategoriesByType(type)
            .map { categoryList ->
                val categoryMap = mutableMapOf<Long, Category>()
                categoryList.forEach { categoryMap[it.id] = it }
                categoryMap
            }
            .flatMapConcat { categoryMap ->
                transactionRepo.getTransactionsByTypeWithoutDate(from, to, type)
                    .map { transactions ->
                        transactions.map { transaction ->
                            val category = categoryMap[transaction.categoryId]
                                ?: throw CategoryNotFoundException("Not found category with id: ${transaction.categoryId}")
                            transaction.toDomainModel(category)
                        }
                    }
            }

    override fun getSumSpentInFiscalPeriod(): Flow<BigDecimal> {
        return flow { emit(userInteractor.getFiscalDay()) }.flatMapConcat { fiscalDay ->
            val endDate = Date()
            val startDate = endDate.getStartDateOfFiscalPeriod(fiscalDay)
            transactionRepo.getTransactionsByTypeWithoutDate(
                startDate,
                endDate,
                TransactionType.EXPENSE
            ).map { list -> list.sumOf { it.sum } }
        }
    }

    override fun getSumEarnedInFiscalPeriod(): Flow<BigDecimal> {
        return flow { emit(userInteractor.getFiscalDay()) }.flatMapConcat { fiscalDay ->
            val endDate = Date()
            val startDate = endDate.getStartDateOfFiscalPeriod(fiscalDay)
            transactionRepo.getTransactionsByTypeWithoutDate(
                startDate,
                endDate,
                TransactionType.INCOME
            ).map { list -> list.sumOf { it.sum } }
        }
    }

    override suspend fun update(transaction: Transaction) =
        transactionRepo.update(transaction.toDataModel())

    override suspend fun removeTransaction(transaction: Transaction) {
        transactionRepo.removeTransaction(transaction.toDataModel())
        val countByCode = transactionRepo.getCountByCurrencyCode(transaction.currencyCode)
        if (countByCode == 0) {
            val currency = currencyInteractor.getCurrencyByCode(transaction.currencyCode)
            currencyInteractor.updateCurrency(currency.copy(used = false))
        }
    }

    override suspend fun removeAllByCategory(category: Category) {
        transactionRepo.getByCategory(category).collect { list ->
            val currencySet = mutableSetOf<String>()
            list.forEach { transaction: TransactionDataModel -> currencySet.add(transaction.currency) }
            transactionRepo.removeAllByCategory(category)
            for (item in currencySet) {
                val countByCode = transactionRepo.getCountByCurrencyCode(item)
                if (countByCode == 0) {
                    val currency = currencyInteractor.getCurrencyByCode(item)
                    currencyInteractor.updateCurrency(currency.copy(used = false))
                }
            }
        }
    }

    override suspend fun clearAll() {
        transactionRepo.clearAll()
    }
}
