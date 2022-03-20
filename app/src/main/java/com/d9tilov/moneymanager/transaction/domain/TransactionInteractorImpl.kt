package com.d9tilov.moneymanager.transaction.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.exception.CategoryNotFoundException
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_CODE
import com.d9tilov.moneymanager.core.util.countDaysRemainingNextFiscalDate
import com.d9tilov.moneymanager.core.util.currentDate
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.core.util.divideBy
import com.d9tilov.moneymanager.core.util.getEndDateOfFiscalPeriod
import com.d9tilov.moneymanager.core.util.getEndOfDay
import com.d9tilov.moneymanager.core.util.getStartDateOfFiscalPeriod
import com.d9tilov.moneymanager.core.util.getStartOfDay
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.entity.ExecutionPeriod
import com.d9tilov.moneymanager.regular.domain.entity.PeriodType
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionChartModel
import com.d9tilov.moneymanager.transaction.domain.mapper.toChartModel
import com.d9tilov.moneymanager.transaction.domain.mapper.toDataModel
import com.d9tilov.moneymanager.transaction.domain.mapper.toDomainModel
import com.d9tilov.moneymanager.transaction.isIncome
import com.d9tilov.moneymanager.user.domain.UserInteractor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.periodUntil
import kotlinx.datetime.plus
import java.math.BigDecimal
import java.util.Calendar
import java.util.GregorianCalendar

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
        val newTransaction =
            transaction.copy(currencyCode = currencyCode, usdSum = usdSumValue).toDataModel()
        transactionRepo.addTransaction(newTransaction)
        val category = categoryInteractor.getCategoryById(transaction.category.id)
        val count = category.usageCount + 1
        categoryInteractor.update(category.copy(usageCount = count))
        val budget = budgetInteractor.get().first()
        var budgetSum = budget.sum
        budgetSum += currencyInteractor.toMainCurrency(
            if (transaction.type.isIncome()) transaction.sum else transaction.sum.negate(),
            transaction.currencyCode
        )
        budgetInteractor.update(budget.copy(sum = budgetSum))
    }

    override suspend fun getCurrentCurrencyCode(): String =
        currencyInteractor.getCurrentCurrencyCode()

    override fun getTransactionsGroupedByCategory(
        type: TransactionType,
        from: LocalDateTime,
        to: LocalDateTime,
        currencyCode: String,
        inStatistics: Boolean,
        onlySubcategories: Boolean
    ): Flow<List<TransactionChartModel>> {
        return categoryInteractor.getGroupedCategoriesByType(type)
            .flatMapLatest { categoryList ->
                transactionRepo.getTransactionsByTypeInPeriod(from, to, type, inStatistics)
                    .map {
                        it.map { item ->
                            val category =
                                categoryList.find { listItem -> item.categoryId == listItem.id }
                                    ?: throw CategoryNotFoundException("Not found category with id: ${item.categoryId}")
                            item.toChartModel(
                                category,
                                currencyCode,
                                if (currencyCode == DEFAULT_CURRENCY_CODE) item.usdSum
                                else currencyInteractor.toMainCurrency(item.sum, item.currencyCode)
                            )
                        }
                    }
                    .map { list ->
                        val sum = list.sumOf { tr -> tr.sum }
                        list.groupBy { tr ->
                            if (onlySubcategories) tr.category
                            else tr.category.parent ?: tr.category
                        }
                            .map { entry: Map.Entry<Category, List<TransactionChartModel>> ->
                                val currencySum: BigDecimal = entry.value.sumOf { item -> item.sum }
                                val transaction: TransactionChartModel = entry.value.first()
                                TransactionChartModel(
                                    transaction.clientId,
                                    transaction.type,
                                    entry.key,
                                    currencyCode,
                                    currencySum,
                                    currencySum.divideBy(sum).multiply(BigDecimal(100)),
                                    inStatistics
                                )
                            }
                    }
            }
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
                    transactionDataModel.currencyCode,
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
                    list.sumOf { currencyInteractor.toMainCurrency(it.sum, it.currencyCode) }
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
                    currencyInteractor.toMainCurrency(
                        it.sum,
                        it.currencyCode
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
            list.sumOf { currencyInteractor.toMainCurrency(it.sum, it.currencyCode) }
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
            when (tr.executionPeriod.periodType) {
                PeriodType.MONTH -> currencyInteractor.toMainCurrency(
                    tr.sum,
                    tr.currencyCode
                )
                PeriodType.WEEK -> {
                    var dayOfWeekCount = 0
                    var dateIterator = startDate
                    while (dateIterator != endDate) {
                        if (dateIterator.dayOfWeek.ordinal == (tr.executionPeriod as ExecutionPeriod.EveryWeek).dayOfWeek) dayOfWeekCount++
                        dateIterator = dateIterator.plus(1, DateTimeUnit.DAY)
                    }
                    currencyInteractor.toMainCurrency(
                        tr.sum.multiply(BigDecimal(dayOfWeekCount)),
                        tr.currencyCode
                    )
                }
                PeriodType.DAY -> {
                    val countDays = startDate.periodUntil(endDate).days
                    currencyInteractor.toMainCurrency(
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
                list.forEach { tr -> currencies.add(tr.currencyCode) }
                val currencyCode = userInteractor.getCurrentCurrency()
                if ((currencies.size == 1 && currencies.contains(currencyCode)) || currentCurrency == DataConstants.DEFAULT_CURRENCY_CODE) BigDecimal.ZERO
                else list.sumOf { it.usdSum }
            }
        }
    }

    override fun getSumInFiscalPeriod(): Flow<BigDecimal> {
        val incomesFlow =
            flow { emit(userInteractor.getFiscalDay()) }.flatMapConcat { fiscalDay ->
                val endDate = currentDateTime().getEndOfDay()
                val startDate = endDate.getStartDateOfFiscalPeriod(fiscalDay)
                transactionRepo.getTransactionsByTypeInPeriod(
                    startDate,
                    endDate,
                    TransactionType.INCOME,
                    onlyInStatistics = true,
                    withRegular = true
                ).map { list ->
                    list.sumOf { currencyInteractor.toMainCurrency(it.sum, it.currencyCode) }
                }
            }
        val expenseFlow = flow { emit(userInteractor.getFiscalDay()) }.flatMapConcat { fiscalDay ->
            val endDate = currentDateTime().getEndOfDay()
            val startDate = endDate.getStartDateOfFiscalPeriod(fiscalDay)
            transactionRepo.getTransactionsByTypeInPeriod(
                startDate,
                endDate,
                TransactionType.EXPENSE,
                onlyInStatistics = true,
                withRegular = true
            ).map { list ->
                list.sumOf {
                    currencyInteractor.toMainCurrency(
                        it.sum,
                        it.currencyCode
                    )
                }
            }
        }
        return combine(
            incomesFlow,
            expenseFlow
        ) { income, expense -> income.minus(expense) }
    }

    override fun getSumTodayInUsd(type: TransactionType): Flow<BigDecimal> =
        transactionRepo.getTransactionsByTypeInPeriod(
            currentDateTime().getStartOfDay(),
            currentDateTime().getEndOfDay(),
            type
        ).map { list ->
            val currentCurrency = userInteractor.getCurrentCurrency()
            val currencies = mutableSetOf<String>()
            list.forEach { tr -> currencies.add(tr.currencyCode) }
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
                    if (tr.currencyCode == currentCurrency) {
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
                if (tr.currencyCode == currentCurrency) {
                    tr.sum
                } else {
                    val trCurrency = currencyInteractor.getCurrencyByCode(currentCurrency)
                    trCurrency.value.multiply(tr.usdSum)
                }
            }
        }

    override suspend fun executeRegularIfNeeded(type: TransactionType) {
        regularTransactionInteractor.getAll(type).map { transactions ->
            for (tr in transactions) {
                val curDay = currentDate()
                when (tr.executionPeriod.periodType) {
                    PeriodType.DAY -> {
                        var dayIterator = tr.executionPeriod.lastExecutionDateTime.date
                        val listOfSkippedDates = mutableListOf<LocalDate>()
                        while (dayIterator <= curDay) {
                            dayIterator = dayIterator.plus(1, DateTimeUnit.DAY)
                            listOfSkippedDates.add(dayIterator)
                        }
                        listOfSkippedDates.forEachIndexed { index, day ->
                            val transaction = Transaction(
                                type = tr.type,
                                sum = tr.sum,
                                category = tr.category,
                                currencyCode = tr.currencyCode,
                                date = day.getStartOfDay(),
                                description = tr.description,
                                isRegular = true,
                                inStatistics = true
                            )
                            GlobalScope.launch {
                                addTransaction(transaction)
                                if (index == listOfSkippedDates.size - 1) {
                                    regularTransactionInteractor.update(
                                        tr.copy(
                                            executionPeriod = ExecutionPeriod.EveryDay(
                                                day.getStartOfDay()
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }
                    PeriodType.WEEK -> {
                        var dayIterator = tr.executionPeriod.lastExecutionDateTime.date
                        val executeDay = (tr.executionPeriod as ExecutionPeriod.EveryWeek).dayOfWeek
                        val listOfSkippedDates = mutableListOf<LocalDate>()
                        while (dayIterator <= curDay) {
                            dayIterator = dayIterator.plus(1, DateTimeUnit.DAY)
                            if (dayIterator.dayOfWeek.ordinal == executeDay) {
                                listOfSkippedDates.add(dayIterator)
                            }
                        }
                        listOfSkippedDates.forEachIndexed { index, day ->
                            val transaction = Transaction(
                                type = tr.type,
                                sum = tr.sum,
                                category = tr.category,
                                currencyCode = tr.currencyCode,
                                date = day.getStartOfDay(),
                                description = tr.description,
                                isRegular = true,
                                inStatistics = true
                            )
                            GlobalScope.launch {
                                addTransaction(transaction)
                                if (index == listOfSkippedDates.size - 1) {
                                    regularTransactionInteractor.update(
                                        tr.copy(
                                            executionPeriod = ExecutionPeriod.EveryWeek(
                                                tr.executionPeriod.dayOfWeek,
                                                day.getStartOfDay()
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }
                    PeriodType.MONTH -> {
                        var dayIterator = tr.executionPeriod.lastExecutionDateTime.date
                        val executeDay =
                            (tr.executionPeriod as ExecutionPeriod.EveryMonth).dayOfMonth
                        val listOfSkippedDates = mutableListOf<LocalDate>()
                        while (dayIterator <= curDay) {
                            dayIterator = dayIterator.plus(1, DateTimeUnit.DAY)
                            val c = GregorianCalendar(
                                dayIterator.year,
                                dayIterator.monthNumber - 1,
                                dayIterator.dayOfMonth
                            )
                            val countDaysOfMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH)
                            if (executeDay > countDaysOfMonth) {
                                if (dayIterator.dayOfMonth == countDaysOfMonth) {
                                    listOfSkippedDates.add(dayIterator)
                                }
                            } else {
                                if (dayIterator.dayOfMonth == executeDay) {
                                    listOfSkippedDates.add(dayIterator)
                                }
                            }
                        }
                        listOfSkippedDates.forEachIndexed { index, day ->
                            val transaction = Transaction(
                                type = tr.type,
                                sum = tr.sum,
                                category = tr.category,
                                currencyCode = tr.currencyCode,
                                date = day.getStartOfDay(),
                                description = tr.description,
                                isRegular = true,
                                inStatistics = true
                            )
                            GlobalScope.launch {
                                addTransaction(transaction)
                                if (index == listOfSkippedDates.size - 1) {
                                    regularTransactionInteractor.update(
                                        tr.copy(
                                            executionPeriod = ExecutionPeriod.EveryMonth(
                                                tr.executionPeriod.dayOfMonth,
                                                day.getStartOfDay()
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }.first()
    }

    override suspend fun update(transaction: Transaction) {
        val usdSumValue = currencyInteractor.toUsd(transaction.sum, transaction.currencyCode)
        val oldTransaction: TransactionDataModel =
            transactionRepo.getTransactionById(transaction.id).first()
        transactionRepo.update(transaction.toDataModel().copy(usdSum = usdSumValue))
        val budget = budgetInteractor.get().first()
        var budgetSum = budget.sum
        budgetSum += currencyInteractor.toMainCurrency(
            if (oldTransaction.type.isIncome()) oldTransaction.sum.negate() else oldTransaction.sum,
            oldTransaction.currencyCode
        )
        budgetSum += currencyInteractor.toMainCurrency(
            if (transaction.type.isIncome()) transaction.sum else transaction.sum.negate(),
            transaction.currencyCode
        )
        budgetInteractor.update(budget.copy(sum = budgetSum))
    }

    override suspend fun removeTransaction(transaction: Transaction) {
        transactionRepo.removeTransaction(transaction.toDataModel())
        val budget = budgetInteractor.get().first()
        var budgetSum = budget.sum
        budgetSum += currencyInteractor.toMainCurrency(
            if (transaction.type.isIncome()) transaction.sum.negate() else transaction.sum,
            transaction.currencyCode
        )
        budgetInteractor.update(budget.copy(sum = budgetSum))
    }

    override fun removeAllByCategory(category: Category): Flow<Int> {
        return transactionRepo.getByCategory(category)
            .map { list: List<TransactionDataModel> ->
                list.forEach { tr -> removeTransaction(tr.toDomainModel(category)) }
                list.size
            }
    }

    override suspend fun clearAll() {
        transactionRepo.clearAll()
    }
}
