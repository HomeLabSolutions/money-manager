package com.d9tilov.moneymanager.transaction.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.exception.CategoryNotFoundException
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.util.countDaysRemainingNextFiscalDate
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.core.util.divideBy
import com.d9tilov.moneymanager.core.util.getEndDateOfFiscalPeriod
import com.d9tilov.moneymanager.core.util.getEndOfDay
import com.d9tilov.moneymanager.core.util.getStartDateOfFiscalPeriod
import com.d9tilov.moneymanager.core.util.getStartOfDay
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.period.PeriodType
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.transaction.TransactionType
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
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.periodUntil
import kotlinx.datetime.plus
import java.math.BigDecimal

class TransactionInteractorImpl(
    private val transactionRepo: TransactionRepo,
    private val regularTransactionInteractor: RegularTransactionInteractor,
    private val categoryInteractor: CategoryInteractor,
    private val userInteractor: UserInteractor,
    private val currencyInteractor: CurrencyInteractor,
    private val budgetInteractor: BudgetInteractor
) : TransactionInteractor {

    override suspend fun addTransaction(transaction: Transaction) {
        val currencyCode = transaction.currencyCode
        val usdSumValue = currencyInteractor.toUsd(transaction.sum, currencyCode)
        val newTransaction = transaction.copy(currencyCode = currencyCode, usdSum = usdSumValue).toDataModel()
        transactionRepo.addTransaction(newTransaction)
        val category = categoryInteractor.getCategoryById(transaction.category.id)
        val count = category.usageCount + 1
        categoryInteractor.update(category.copy(usageCount = count))
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

    override fun getTransactionsByType(type: TransactionType): Flow<PagingData<Transaction>> {
        return categoryInteractor.getGroupedCategoriesByType(type)
            .flatMapLatest { categoryList ->
                transactionRepo.getTransactionsByType(transactionType = type)
                    .map {
                        it.map { item ->
                            val category =
                                categoryList.find { listItem -> item.categoryId == listItem.id }
                                    ?: throw CategoryNotFoundException("Not found category with id: ${item.categoryId}")
                            item.toDomainModel(category)
                        }
                    }
            }
    }

    override fun ableToSpendToday(): Flow<BigDecimal> {
        val regularIncomeSumFlow =
            flow { emit(userInteractor.getFiscalDay()) }.flatMapConcat { fiscalDay ->
                regularTransactionInteractor.getAll(TransactionType.INCOME).map { incomes ->
                    getSumOfRegularTransactions(fiscalDay, incomes)
                }
            }
        val regularExpenseSumFlow =
            flow { emit(userInteractor.getFiscalDay()) }.flatMapConcat { fiscalDay ->
                regularTransactionInteractor.getAll(TransactionType.EXPENSE)
                    .map { expenses -> getSumOfRegularTransactions(fiscalDay, expenses) }
            }
        val incomesFlow =
            flow { emit(userInteractor.getFiscalDay()) }.flatMapConcat { fiscalDay ->
                val endDate = currentDateTime().getEndOfDay()
                val startDate = endDate.getStartDateOfFiscalPeriod(fiscalDay)
                transactionRepo.getTransactionsByTypeInPeriod(
                    startDate,
                    endDate,
                    TransactionType.INCOME,
                    onlyInStatistics = true,
                    withRegular = false
                ).map { list ->
                    list.sumOf { currencyInteractor.convertToMainCurrency(it.sum, it.currency) }
                }
            }
        val expenseFlow = flow { emit(userInteractor.getFiscalDay()) }.flatMapConcat { fiscalDay ->
            val endDate = currentDateTime()
            val endDateMinusDay =
                endDate.date.minus(1, DateTimeUnit.DAY).atTime(0, 0, 0, 0).getEndOfDay()
            val startDate = endDate.getStartDateOfFiscalPeriod(fiscalDay)
            transactionRepo.getTransactionsByTypeInPeriod(
                startDate,
                endDateMinusDay,
                TransactionType.EXPENSE,
                onlyInStatistics = true,
                withRegular = false
            ).map { list ->
                list.sumOf {
                    currencyInteractor.convertToMainCurrency(
                        it.sum,
                        it.currency
                    )
                }
            }
        }

        val savedSumPerPeriodFlow = budgetInteractor.get().map { it.saveSum }
        val countDaysSinceFiscalDateFlow: Flow<BigDecimal> =
            flow { emit(userInteractor.getFiscalDay()) }.map { fiscalDay ->
                currentDateTime().countDaysRemainingNextFiscalDate(fiscalDay).toBigDecimal()
            }

        val expensesPerCurrentDayFlow = transactionRepo.getTransactionsByTypeInPeriod(
            currentDateTime().getStartOfDay(),
            currentDateTime().getEndOfDay(),
            TransactionType.EXPENSE,
            onlyInStatistics = true,
            withRegular = false
        ).map { list ->
            list.sumOf { currencyInteractor.convertToMainCurrency(it.sum, it.currency) }
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

    private suspend fun getSumOfRegularTransactions(
        fiscalDay: Int,
        transactions: List<RegularTransaction>
    ): BigDecimal {
        val curDate = currentDateTime().getEndOfDay()
        val startDate = curDate.getStartDateOfFiscalPeriod(fiscalDay).date
        val endDate = curDate.getEndDateOfFiscalPeriod(fiscalDay).date
        return transactions.sumOf { tr ->
            when (tr.periodType) {
                PeriodType.MONTH -> currencyInteractor.convertToMainCurrency(
                    tr.sum,
                    tr.currencyCode
                )
                PeriodType.WEEK -> {
                    var dayOfWeekCount = 0
                    var dateIterator = startDate
                    while (dateIterator != endDate) {
                        if (dateIterator.dayOfWeek.ordinal == tr.dayOfWeek) dayOfWeekCount++
                        dateIterator = dateIterator.plus(1, DateTimeUnit.DAY)
                    }
                    currencyInteractor.convertToMainCurrency(
                        tr.sum.multiply(BigDecimal(dayOfWeekCount)),
                        tr.currencyCode
                    )
                }
                PeriodType.DAY -> {
                    val countDays = startDate.periodUntil(endDate).days
                    currencyInteractor.convertToMainCurrency(
                        tr.sum.multiply(BigDecimal(countDays)),
                        tr.currencyCode
                    )
                }
            }
        }
    }

    override fun getSumInFiscalPeriodInUsd(type: TransactionType): Flow<BigDecimal> {
        return flow { emit(userInteractor.getFiscalDay()) }.flatMapConcat { fiscalDay ->
            val endDate = currentDateTime()
            val startDate = endDate.getStartDateOfFiscalPeriod(fiscalDay)
            transactionRepo.getTransactionsByTypeInPeriod(
                startDate,
                endDate,
                type
            ).map { list ->
                val currentCurrency = userInteractor.getCurrentCurrency()
                val currencies = mutableSetOf<String>()
                list.forEach { tr -> currencies.add(tr.currency) }
                val currencyCode = userInteractor.getCurrentCurrency()
                if ((currencies.size == 1 && currencies.contains(currencyCode)) || currentCurrency == DataConstants.DEFAULT_CURRENCY_CODE) BigDecimal.ZERO
                else list.sumOf { it.usdSum }
            }
        }
    }

    override fun getSumTodayInUsd(type: TransactionType): Flow<BigDecimal> =
        transactionRepo.getTransactionsByTypeInPeriod(
            currentDateTime().getStartOfDay(),
            currentDateTime().getEndOfDay(),
            type
        ).map { list ->
            val currentCurrency = userInteractor.getCurrentCurrency()
            val currencies = mutableSetOf<String>()
            list.forEach { tr -> currencies.add(tr.currency) }
            val currencyCode = userInteractor.getCurrentCurrency()
            if ((currencies.size == 1 && currencies.contains(currencyCode)) || currentCurrency == DataConstants.DEFAULT_CURRENCY_CODE) BigDecimal.ZERO
            else list.sumOf { it.usdSum }
        }

    override fun getApproxSumInFiscalPeriodCurrentCurrency(type: TransactionType): Flow<BigDecimal> {
        return flow { emit(userInteractor.getFiscalDay()) }.flatMapConcat { fiscalDay ->
            val endDate = currentDateTime()
            val startDate = endDate.getStartDateOfFiscalPeriod(fiscalDay)
            transactionRepo.getTransactionsByTypeInPeriod(
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

    override fun getApproxSumTodayCurrentCurrency(type: TransactionType): Flow<BigDecimal> =
        transactionRepo.getTransactionsByTypeInPeriod(
            currentDateTime().getStartOfDay(),
            currentDateTime().getEndOfDay(),
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

    override suspend fun executeRegularIfNeeded() {
        TODO("Not yet implemented")
    }

    override suspend fun update(transaction: Transaction) {
        val usdSumValue = currencyInteractor.toUsd(transaction.sum, transaction.currencyCode)
        transactionRepo.update(transaction.toDataModel().copy(usdSum = usdSumValue))
    }

    override suspend fun removeTransaction(transaction: Transaction) {
        transactionRepo.removeTransaction(transaction.toDataModel())
    }

    override fun removeAllByCategory(category: Category): Flow<Int> {
        return transactionRepo.getByCategory(category).flatMapConcat {
            transactionRepo.removeAllByCategory(category)
        }
    }

    override suspend fun clearAll() {
        transactionRepo.clearAll()
    }
}
