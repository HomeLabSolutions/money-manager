package com.d9tilov.moneymanager.transaction.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.exception.CategoryNotFoundException
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.util.countDaysRemainingNexFiscalDate
import com.d9tilov.moneymanager.core.util.divideBy
import com.d9tilov.moneymanager.core.util.getEndOfDay
import com.d9tilov.moneymanager.core.util.getStartDateOfFiscalPeriod
import com.d9tilov.moneymanager.core.util.getStartOfDay
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.exchanger.domain.ExchangeInteractor
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDateDataModel
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.domain.mapper.toDataModel
import com.d9tilov.moneymanager.transaction.domain.mapper.toDomainModel
import com.d9tilov.moneymanager.user.domain.UserInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.util.Calendar
import java.util.Date

class TransactionInteractorImpl(
    private val transactionRepo: TransactionRepo,
    private val regularTransactionInteractor: RegularTransactionInteractor,
    private val categoryInteractor: CategoryInteractor,
    private val userInteractor: UserInteractor,
    private val currencyInteractor: CurrencyInteractor,
    private val exchangeInteractor: ExchangeInteractor,
    private val budgetInteractor: BudgetInteractor
) : TransactionInteractor {

    override suspend fun addTransaction(transaction: Transaction) {
        val currencyCode = userInteractor.getCurrentCurrency()
        val usdSumValue = exchangeInteractor.toUsd(transaction.sum, currencyCode)
        val newTransaction = transaction.copy(currencyCode = currencyCode, usdSum = usdSumValue).toDataModel()
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
                    category,
                    transactionDataModel.currency,
                    transactionDataModel.sum,
                    transactionDataModel.usdSum,
                    transactionDataModel.date,
                    transactionDataModel.description,
                    transactionDataModel.qrCode
                )
            }
    }

    override fun getTransactionsByType(type: TransactionType): Flow<PagingData<BaseTransaction>> {
        return categoryInteractor.getGroupedCategoriesByType(type)
            .flatMapLatest { categoryList ->
                transactionRepo.getTransactionsByType(transactionType = type)
                    .map {
                        it.map { item ->
                            when (item) {
                                is TransactionDateDataModel -> item.toDomainModel()
                                is TransactionDataModel -> {
                                    val category =
                                        categoryList.find { listItem -> item.categoryId == listItem.id }
                                            ?: throw CategoryNotFoundException("Not found category with id: ${item.categoryId}")
                                    item.toDomainModel(category)
                                }
                                else -> throw IllegalStateException("Unknown TransactionDataItem implementation: $item")
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

    override fun ableToSpendToday(): Flow<BigDecimal> {
        val regularIncomeSumFlow =
            regularTransactionInteractor.getAll(TransactionType.INCOME).map { incomes ->
                incomes.sumOf {
                    exchangeInteractor.convertToMainCurrency(
                        it.sum,
                        it.currencyCode
                    )
                }
            }
        val regularExpenseSumFlow =
            regularTransactionInteractor.getAll(TransactionType.EXPENSE).map { expenses ->
                expenses.sumOf {
                    exchangeInteractor.convertToMainCurrency(
                        it.sum,
                        it.currencyCode
                    )
                }
            }
        val incomesFlow = flow { emit(userInteractor.getFiscalDay()) }.flatMapConcat { fiscalDay ->
            val endDate = Date().getEndOfDay()
            val startDate = endDate.getStartDateOfFiscalPeriod(fiscalDay)
            transactionRepo.getTransactionsByTypeWithoutDate(
                startDate,
                endDate,
                TransactionType.INCOME
            ).map { list ->
                list.sumOf {
                    exchangeInteractor.convertToMainCurrency(
                        it.sum,
                        it.currency
                    )
                }
            }
        }
        val expenseFlow = flow { emit(userInteractor.getFiscalDay()) }.flatMapConcat { fiscalDay ->
            val c = Calendar.getInstance()
            val endDate = c.time
            c.add(Calendar.DATE, -1)
            val endDateMinusDay = c.time.getEndOfDay()
            val startDate = endDate.getStartDateOfFiscalPeriod(fiscalDay)
            transactionRepo.getTransactionsByTypeWithoutDate(
                startDate,
                endDateMinusDay,
                TransactionType.EXPENSE
            ).map { list ->
                list.sumOf {
                    exchangeInteractor.convertToMainCurrency(
                        it.sum,
                        it.currency
                    )
                }
            }
        }

        val savedSumPerPeriodFlow = budgetInteractor.get().map { it.saveSum }
        val countDaysSinceFiscalDateFlow: Flow<BigDecimal> =
            flow { emit(userInteractor.getFiscalDay()) }.map { fiscalDay ->
                Date().countDaysRemainingNexFiscalDate(fiscalDay).toBigDecimal()
            }

        val expensesPerCurrentDayFlow = transactionRepo.getTransactionsByTypeWithoutDate(
            Date().getStartOfDay(),
            Date().getEndOfDay(),
            TransactionType.EXPENSE
        ).map { list ->
            list.sumOf {
                exchangeInteractor.convertToMainCurrency(
                    it.sum,
                    it.currency
                )
            }
        }

        val numeratorFlow = combine(
            regularIncomeSumFlow,
            regularExpenseSumFlow,
            savedSumPerPeriodFlow,
            incomesFlow,
            expenseFlow
        ) { regularIncomes, regularExpenses, savedSumPerPeriod, incomes, expenses ->
            regularIncomes.minus(regularExpenses).minus(savedSumPerPeriod).plus(incomes)
                .minus(expenses)
        }
        return combine(
            numeratorFlow,
            countDaysSinceFiscalDateFlow,
            expensesPerCurrentDayFlow
        ) { numerator, countDaysSinceFiscalDate, expensesPerCurrentDay ->
            numerator.divideBy(countDaysSinceFiscalDate).minus(expensesPerCurrentDay)
        }
    }

    override fun getSumInFiscalPeriodInUsd(type: TransactionType): Flow<BigDecimal> {
        return flow { emit(userInteractor.getFiscalDay()) }.flatMapConcat { fiscalDay ->
            val endDate = Date()
            val startDate = endDate.getStartDateOfFiscalPeriod(fiscalDay)
            transactionRepo.getTransactionsByTypeWithoutDate(
                startDate,
                endDate,
                type
            ).map { list ->
                val currentCurrency = userInteractor.getCurrentCurrency()
                val currencies = mutableSetOf<String>()
                list.forEach { tr -> currencies.add(tr.currency) }
                if (currencies.size == 1 || currentCurrency == DataConstants.DEFAULT_CURRENCY_CODE) BigDecimal.ZERO
                else list.sumOf { it.usdSum }
            }
        }
    }

    override fun getApproxSumInFiscalPeriodCurrentCurrency(type: TransactionType): Flow<BigDecimal> {
        return flow { emit(userInteractor.getFiscalDay()) }.flatMapConcat { fiscalDay ->
            val endDate = Date()
            val startDate = endDate.getStartDateOfFiscalPeriod(fiscalDay)
            transactionRepo.getTransactionsByTypeWithoutDate(
                startDate,
                endDate,
                type
            ).map { list ->
                val currentCurrency = userInteractor.getCurrentCurrency()
                list.sumOf { tr ->
                    if (tr.currency == currentCurrency) {
                        tr.sum
                    } else {
                        val trCurrency = currencyInteractor.getCurrencyByCode(currentCurrency)
                        trCurrency.value.multiply(tr.usdSum)
                    }
                }
            }
        }
    }

    override suspend fun update(transaction: Transaction) {
        val usdSumValue = exchangeInteractor.toUsd(transaction.sum, transaction.currencyCode)
        transactionRepo.update(transaction.toDataModel().copy(usdSum = usdSumValue))
    }

    override suspend fun removeTransaction(transaction: Transaction) {
        transactionRepo.removeTransaction(transaction.toDataModel())
        val countByCode = transactionRepo.getCountByCurrencyCode(transaction.currencyCode)
        if (countByCode == 0) {
            val currency = currencyInteractor.getCurrencyByCode(transaction.currencyCode)
            currencyInteractor.updateCurrency(currency.copy(used = false))
        }
    }

    override fun removeAllByCategory(category: Category): Flow<Int> {
        return transactionRepo.getByCategory(category).flatMapConcat { list ->
            val currencySet = mutableSetOf<String>()
            list.forEach { transaction: TransactionDataModel -> currencySet.add(transaction.currency) }
            for (item in currencySet) {
                val countByCode = transactionRepo.getCountByCurrencyCode(item)
                if (countByCode == 0) {
                    val currency = currencyInteractor.getCurrencyByCode(item)
                    currencyInteractor.updateCurrency(currency.copy(used = false))
                }
            }
            transactionRepo.removeAllByCategory(category)
        }
    }

    override suspend fun clearAll() {
        transactionRepo.clearAll()
    }
}
