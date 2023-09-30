package com.d9tilov.android.transaction.domain.impl

import androidx.paging.PagingData
import androidx.paging.map
import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.domain.model.exception.CategoryException
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.model.PeriodType
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.model.isIncome
import com.d9tilov.android.core.utils.countDaysRemainingNextFiscalDate
import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.divideBy
import com.d9tilov.android.core.utils.getEndDateOfFiscalPeriod
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.core.utils.getStartDateOfFiscalPeriod
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.core.utils.isSameDay
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.regular.transaction.domain.model.RegularTransaction
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.contract.TransactionRepo
import com.d9tilov.android.transaction.domain.impl.mapper.toChartModel
import com.d9tilov.android.transaction.domain.impl.mapper.toDataModel
import com.d9tilov.android.transaction.domain.impl.mapper.toDomainModel
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.domain.model.TransactionChartModel
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import com.d9tilov.android.transaction.domain.model.TransactionLineChartModel
import com.d9tilov.android.transaction.domain.model.TransactionSpendingTodayModel
import com.d9tilov.android.user.domain.contract.UserInteractor
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
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
        coroutineScope {
            launch { transactionRepo.addTransaction(newTransaction) }
            launch {
                val category = categoryInteractor.getCategoryById(transaction.category.id)
                val count = category.usageCount + 1
                categoryInteractor.update(category.copy(usageCount = count))
            }
            launch {
                val budget = budgetInteractor.get().first()
                var budgetSum = budget.sum
                budgetSum += currencyInteractor.toTargetCurrency(
                    if (transaction.type.isIncome()) transaction.sum else transaction.sum.negate(),
                    currencyCode,
                    currencyInteractor.getMainCurrency().code
                )
                budgetInteractor.update(budget.copy(sum = budgetSum))
            }
        }
    }

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
                val parentChildrenMap: Map<Category, List<Category>> =
                    categoryList.groupBy { category -> category.parent ?: category }
                transactionRepo.getTransactionsByTypeInPeriod(from, to, type, inStatistics)
                    .map {
                        it.map { item ->
                            val category =
                                categoryList.find { listItem -> item.categoryId == listItem.id }
                                    ?: throw CategoryException.CategoryNotFoundException("getTransactionsGroupedByCategory Not found category with id: ${item.categoryId}")
                            item.toChartModel(
                                category,
                                currencyCode,
                                if (currencyCode == DEFAULT_CURRENCY_CODE) {
                                    item.usdSum
                                } else {
                                    currencyInteractor.toTargetCurrency(
                                        item.sum,
                                        item.currencyCode,
                                        currencyInteractor.getMainCurrency().code
                                    )
                                }
                            )
                        }
                    }
                    .map { list ->
                        val sum = list.sumOf { tr -> tr.sum }
                        list.groupBy { tr ->
                            if (onlySubcategories) {
                                tr.category
                            } else {
                                tr.category.parent ?: tr.category
                            }
                        }
                            .mapKeys { entry ->
                                val category = entry.key
                                category.copy(children = parentChildrenMap[category] ?: emptyList())
                            }
                            .map { entry: Map.Entry<Category, List<TransactionChartModel>> ->
                                val currencySum: BigDecimal = entry.value.sumOf { item -> item.sum }
                                val transaction: TransactionChartModel = entry.value.first()
                                val category = entry.key
                                TransactionChartModel(
                                    transaction.clientId,
                                    transaction.type,
                                    category,
                                    currencyCode,
                                    currencySum,
                                    currencySum.divideBy(sum)
                                        .multiply(BigDecimal(PERCENT_FULL_COUNT))
                                )
                            }
                    }
            }
    }

    override fun getTransactionsGroupedByDate(
        type: TransactionType,
        from: LocalDateTime,
        to: LocalDateTime,
        currencyCode: String,
        inStatistics: Boolean
    ): Flow<Map<LocalDateTime, TransactionLineChartModel>> {
        return transactionRepo.getTransactionsByTypeInPeriod(from, to, type, inStatistics)
            .map { list ->
                list.map { item -> item.copy(date = item.date.getStartOfDay()) }.toList()
                    .groupBy { item -> item.date }
                    .toSortedMap()
                    .mapValues { item: Map.Entry<LocalDateTime, List<TransactionDataModel>> ->
                        TransactionLineChartModel(
                            currencyCode = currencyCode,
                            sum = item.value.sumOf { model ->
                                if (currencyCode == DEFAULT_CURRENCY_CODE) {
                                    model.usdSum
                                } else {
                                    currencyInteractor.toTargetCurrency(
                                        model.sum,
                                        model.currencyCode,
                                        currencyInteractor.getMainCurrency().code
                                    )
                                }
                            }
                        )
                    }
            }
    }

    override suspend fun getTransactionsByCategory(
        type: TransactionType,
        category: Category,
        from: LocalDateTime,
        to: LocalDateTime,
        inStatistics: Boolean
    ): List<Transaction> {
        val categoryList: List<Category> = category.children.ifEmpty { listOf(category) }
        return categoryList.flatMap { item: Category ->
            transactionRepo.getByCategoryInPeriod(item, from, to, inStatistics)
                .first()
                .map { tr: TransactionDataModel ->
                    val foundCategory =
                        categoryList.find { listItem -> tr.categoryId == listItem.id }
                            ?: throw CategoryException.CategoryNotFoundException("getTransactionsByCategory Not found category with id: ${tr.categoryId}")
                    tr.toDomainModel(foundCategory)
                }
        }
    }

    override fun getTransactionById(id: Long): Flow<Transaction> {
        return transactionRepo.getTransactionById(id)
            .map { transactionDataModel ->
                val category = categoryInteractor.getCategoryById(transactionDataModel.categoryId)
                Transaction.EMPTY.copy(
                    id = transactionDataModel.id,
                    clientId = transactionDataModel.clientId,
                    type = transactionDataModel.type,
                    category = category,
                    currencyCode = transactionDataModel.currencyCode,
                    sum = transactionDataModel.sum,
                    usdSum = transactionDataModel.usdSum,
                    date = transactionDataModel.date,
                    description = transactionDataModel.description,
                    qrCode = transactionDataModel.qrCode
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
                                    ?: throw CategoryException.CategoryNotFoundException("getTransactionsByType Not found category with id: ${item.categoryId}")
                            item.toDomainModel(category)
                        }
                    }
            }
    }

    override fun ableToSpendToday(): Flow<TransactionSpendingTodayModel> {
        val countDaysSinceFiscalDateFlow: Flow<BigDecimal> =
            flow { emit(userInteractor.getFiscalDay()) }.map { fiscalDay ->
                currentDateTime().countDaysRemainingNextFiscalDate(fiscalDay).toBigDecimal()
            }
        val numeratorFlow = getNumerator()
        val expensesPerCurrentDayFlow = getExpensesPerCurrentDay()

        return combine(
            numeratorFlow,
            countDaysSinceFiscalDateFlow,
            expensesPerCurrentDayFlow
        ) { numerator, countDaysSinceFiscalDate, expensesPerCurrentDay ->
            if (numerator.minus(expensesPerCurrentDay).signum() < 0) {
                TransactionSpendingTodayModel.OVERSPENDING(numerator.minus(expensesPerCurrentDay))
            } else {
                TransactionSpendingTodayModel.NORMAL(
                    numerator.divideBy(countDaysSinceFiscalDate).minus(expensesPerCurrentDay)
                )
            }
        }
    }

    override fun ableToSpendInFiscalPeriod(): Flow<BigDecimal> {
        return combine(
            getNumerator(),
            getExpensesPerCurrentDay()
        ) { numerator, expensesPerCurrentDay -> numerator.minus(expensesPerCurrentDay) }
    }

    private fun getExpensesPerCurrentDay(): Flow<BigDecimal> =
        transactionRepo.getTransactionsByTypeInPeriod(
            currentDateTime().getStartOfDay(),
            currentDateTime().getEndOfDay(),
            TransactionType.EXPENSE,
            onlyInStatistics = true,
            withRegular = false
        ).map { list ->
            list.sumOf {
                currencyInteractor.toTargetCurrency(
                    it.sum,
                    it.currencyCode,
                    currencyInteractor.getMainCurrency().code
                )
            }
        }

    private fun getNumerator(): Flow<BigDecimal> {
        val regularIncomeSumFlow =
            flow { emit(userInteractor.getFiscalDay()) }
                .flatMapMerge { fiscalDay ->
                    regularTransactionInteractor.getAll(TransactionType.INCOME).map { incomes ->
                        getSumOfRegularTransactions(fiscalDay, incomes)
                    }
                }
        val regularExpenseSumFlow =
            flow { emit(userInteractor.getFiscalDay()) }
                .flatMapMerge { fiscalDay ->
                    regularTransactionInteractor.getAll(TransactionType.EXPENSE)
                        .map { expenses -> getSumOfRegularTransactions(fiscalDay, expenses) }
                }
        val incomesFlow =
            flow { emit(userInteractor.getFiscalDay()) }
                .flatMapMerge { fiscalDay ->
                    val endDate = currentDateTime().getEndOfDay()
                    val startDate = getStartDateOfFiscalPeriod(fiscalDay)
                    transactionRepo.getTransactionsByTypeInPeriod(
                        startDate,
                        endDate,
                        TransactionType.INCOME,
                        onlyInStatistics = true,
                        withRegular = false
                    ).map { list ->
                        list.sumOf {
                            currencyInteractor.toTargetCurrency(
                                it.sum,
                                it.currencyCode,
                                currencyInteractor.getMainCurrency().code
                            )
                        }
                    }
                }
        val expenseFlow = flow { emit(userInteractor.getFiscalDay()) }
            .flatMapMerge { fiscalDay ->
                val endDate = currentDateTime()
                val endDateMinusDay =
                    endDate.date.minus(1, DateTimeUnit.DAY).atTime(0, 0, 0, 0).getEndOfDay()
                val startDate = getStartDateOfFiscalPeriod(fiscalDay)
                transactionRepo.getTransactionsByTypeInPeriod(
                    startDate,
                    endDateMinusDay,
                    TransactionType.EXPENSE,
                    onlyInStatistics = true,
                    withRegular = false
                ).map { list ->
                    list.sumOf {
                        currencyInteractor.toTargetCurrency(
                            it.sum,
                            it.currencyCode,
                            currencyInteractor.getMainCurrency().code
                        )
                    }
                }
            }

        val savedSumPerPeriodFlow = budgetInteractor.get().map { it.saveSum }

        return combine(
            regularIncomeSumFlow,
            regularExpenseSumFlow,
            savedSumPerPeriodFlow,
            incomesFlow,
            expenseFlow
        ) { regularIncomes, regularExpenses, savedSumPerPeriod, incomes, expenses ->
            regularIncomes.minus(regularExpenses).minus(savedSumPerPeriod).plus(incomes)
                .minus(expenses)
        }
    }

    private suspend fun getSumOfRegularTransactions(
        fiscalDay: Int,
        transactions: List<RegularTransaction>
    ): BigDecimal {
        val curDate = currentDateTime().getEndOfDay()
        val startDate = getStartDateOfFiscalPeriod(fiscalDay).date
        val endDate = curDate.getEndDateOfFiscalPeriod(fiscalDay).date
        return transactions.sumOf { tr ->
            when (tr.executionPeriod.periodType) {
                PeriodType.MONTH -> currencyInteractor.toTargetCurrency(
                    tr.sum,
                    tr.currencyCode,
                    currencyInteractor.getMainCurrency().code
                )

                PeriodType.WEEK -> {
                    var dayOfWeekCount = 0
                    var dateIterator = startDate
                    while (dateIterator != endDate) {
                        if (dateIterator.dayOfWeek.ordinal == (tr.executionPeriod as ExecutionPeriod.EveryWeek).dayOfWeek) dayOfWeekCount++
                        dateIterator = dateIterator.plus(1, DateTimeUnit.DAY)
                    }
                    currencyInteractor.toTargetCurrency(
                        tr.sum.multiply(BigDecimal(dayOfWeekCount)),
                        tr.currencyCode,
                        currencyInteractor.getMainCurrency().code
                    )
                }

                PeriodType.DAY -> {
                    val countDays = startDate.periodUntil(endDate).days
                    currencyInteractor.toTargetCurrency(
                        tr.sum.multiply(BigDecimal(countDays)),
                        tr.currencyCode,
                        currencyInteractor.getMainCurrency().code
                    )
                }
            }
        }
    }

    override fun isSpendInPeriodApprox(type: TransactionType): Flow<Boolean> =
        flow<Int> { userInteractor.getFiscalDay() }
            .flatMapMerge { fiscalDay ->
                val endDate = currentDateTime()
                val startDate = getStartDateOfFiscalPeriod(fiscalDay)
                isApprox(type, startDate, endDate)
            }

    override fun getSumInFiscalPeriod(): Flow<BigDecimal> {
        val incomesFlow =
            flow { emit(userInteractor.getFiscalDay()) }
                .flatMapMerge { fiscalDay ->
                    val endDate = currentDateTime().getEndOfDay()
                    val startDate = getStartDateOfFiscalPeriod(fiscalDay)
                    transactionRepo.getTransactionsByTypeInPeriod(
                        startDate,
                        endDate,
                        TransactionType.INCOME,
                        onlyInStatistics = true,
                        withRegular = true
                    ).map { list ->
                        list.sumOf {
                            currencyInteractor.toTargetCurrency(
                                it.sum,
                                it.currencyCode,
                                currencyInteractor.getMainCurrency().code
                            )
                        }
                    }
                }
        val expenseFlow = flow { emit(userInteractor.getFiscalDay()) }
            .flatMapMerge { fiscalDay ->
                val endDate = currentDateTime().getEndOfDay()
                val startDate = getStartDateOfFiscalPeriod(fiscalDay)
                transactionRepo.getTransactionsByTypeInPeriod(
                    startDate,
                    endDate,
                    TransactionType.EXPENSE,
                    onlyInStatistics = true,
                    withRegular = true
                ).map { list ->
                    list.sumOf {
                        currencyInteractor.toTargetCurrency(
                            it.sum,
                            it.currencyCode,
                            currencyInteractor.getMainCurrency().code
                        )
                    }
                }
            }
        return combine(
            incomesFlow,
            expenseFlow
        ) { income, expense -> income.minus(expense) }
    }

    override fun isSpendTodayApprox(type: TransactionType): Flow<Boolean> =
        isApprox(type, currentDateTime().getStartOfDay(), currentDateTime().getEndOfDay())

    private fun isApprox(
        type: TransactionType,
        from: LocalDateTime,
        to: LocalDateTime,
    ): Flow<Boolean> =
        combine(
            transactionRepo.getTransactionsByTypeInPeriod(
                from,
                to,
                type
            ),
            currencyInteractor.getMainCurrencyFlow()
        ) { list, currency ->
            val currencies = mutableSetOf<String>()
            list.forEach { tr -> currencies.add(tr.currencyCode) }
            val currencyCode = currency.code
            !((currencies.size == 1 && currencies.contains(currencyCode)) || currencyCode == DEFAULT_CURRENCY_CODE)
        }

    override fun getApproxSumInFiscalPeriodCurrentCurrency(type: TransactionType): Flow<BigDecimal> {
        return currencyInteractor.getMainCurrencyFlow()
            .flatMapMerge { currency ->
                val fiscalDay = userInteractor.getFiscalDay()
                val endDate = currentDateTime()
                val startDate = getStartDateOfFiscalPeriod(fiscalDay)
                transactionRepo.getTransactionsByTypeInPeriod(
                    startDate,
                    endDate,
                    type
                ).map { list ->
                    list.sumOf { tr ->
                        val currencyCode = currency.code
                        if (tr.currencyCode == currencyCode) {
                            tr.sum
                        } else {
                            val trCurrency = currencyInteractor.getCurrencyByCode(currencyCode)
                            trCurrency.value.multiply(tr.usdSum)
                        }
                    }
                }
            }
    }

    override fun getApproxSumTodayCurrentCurrency(type: TransactionType): Flow<BigDecimal> =
        combine(
            transactionRepo.getTransactionsByTypeInPeriod(
                currentDateTime().getStartOfDay(),
                currentDateTime().getEndOfDay(),
                type
            ),
            currencyInteractor.getMainCurrencyFlow()
        ) { list, currency ->
            list.sumOf { tr ->
                val currencyCode = currency.code
                if (tr.currencyCode == currencyCode) {
                    tr.sum
                } else {
                    val trCurrency = currencyInteractor.getCurrencyByCode(currencyCode)
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
                        while (dayIterator <= curDay && !curDay.isSameDay(dayIterator)) {
                            dayIterator = dayIterator.plus(1, DateTimeUnit.DAY)
                            listOfSkippedDates.add(dayIterator)
                        }
                        listOfSkippedDates.forEachIndexed { index, day ->
                            val transaction = Transaction.EMPTY.copy(
                                type = tr.type,
                                sum = tr.sum,
                                category = tr.category,
                                currencyCode = tr.currencyCode,
                                date = day.getStartOfDay(),
                                description = tr.description,
                                isRegular = true,
                                inStatistics = true
                            )
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

                    PeriodType.WEEK -> {
                        var dayIterator = tr.executionPeriod.lastExecutionDateTime.date
                        val executeDay = (tr.executionPeriod as ExecutionPeriod.EveryWeek).dayOfWeek
                        val listOfSkippedDates = mutableListOf<LocalDate>()
                        while (dayIterator <= curDay && !curDay.isSameDay(dayIterator)) {
                            dayIterator = dayIterator.plus(1, DateTimeUnit.DAY)
                            if (dayIterator.dayOfWeek.ordinal == executeDay) {
                                listOfSkippedDates.add(dayIterator)
                            }
                        }
                        listOfSkippedDates.forEachIndexed { index, day ->
                            val transaction = Transaction.EMPTY.copy(
                                type = tr.type,
                                sum = tr.sum,
                                category = tr.category,
                                currencyCode = tr.currencyCode,
                                date = day.getStartOfDay(),
                                description = tr.description,
                                isRegular = true,
                                inStatistics = true
                            )
                            addTransaction(transaction)
                            if (index == listOfSkippedDates.size - 1) {
                                regularTransactionInteractor.update(
                                    tr.copy(
                                        executionPeriod = ExecutionPeriod.EveryWeek(
                                            (tr.executionPeriod as ExecutionPeriod.EveryWeek).dayOfWeek,
                                            day.getStartOfDay()
                                        )
                                    )
                                )
                            }
                        }
                    }

                    PeriodType.MONTH -> {
                        var dayIterator = tr.executionPeriod.lastExecutionDateTime.date
                        val executeDay =
                            (tr.executionPeriod as ExecutionPeriod.EveryMonth).dayOfMonth
                        val listOfSkippedDates = mutableListOf<LocalDate>()
                        while (dayIterator <= curDay && !curDay.isSameDay(dayIterator)) {
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
                            val transaction = Transaction.EMPTY.copy(
                                type = tr.type,
                                sum = tr.sum,
                                category = tr.category,
                                currencyCode = tr.currencyCode,
                                date = day.getStartOfDay(),
                                description = tr.description,
                                isRegular = true,
                                inStatistics = true
                            )
                            addTransaction(transaction)
                            if (index == listOfSkippedDates.size - 1) {
                                regularTransactionInteractor.update(
                                    tr.copy(
                                        executionPeriod = ExecutionPeriod.EveryMonth(
                                            (tr.executionPeriod as ExecutionPeriod.EveryMonth).dayOfMonth,
                                            day.getStartOfDay()
                                        )
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }.first()
    }

    override suspend fun update(transaction: Transaction) {
        coroutineScope {
            launch {
                val usdSumValue =
                    currencyInteractor.toUsd(transaction.sum, transaction.currencyCode)
                transactionRepo.update(transaction.toDataModel().copy(usdSum = usdSumValue))
            }
            launch {
                val oldTransaction = transactionRepo.getTransactionById(transaction.id).first()
                val budget = budgetInteractor.get().first()
                var budgetSum = budget.sum
                budgetSum += currencyInteractor.toTargetCurrency(
                    if (oldTransaction.type.isIncome()) oldTransaction.sum.negate() else oldTransaction.sum,
                    oldTransaction.currencyCode,
                    currencyInteractor.getMainCurrency().code
                )
                budgetSum += currencyInteractor.toTargetCurrency(
                    if (transaction.type.isIncome()) transaction.sum else transaction.sum.negate(),
                    transaction.currencyCode,
                    currencyInteractor.getMainCurrency().code
                )
                budgetInteractor.update(budget.copy(sum = budgetSum))
            }
        }
    }

    override suspend fun removeTransaction(transaction: Transaction) {
        coroutineScope {
            launch { transactionRepo.removeTransaction(transaction.toDataModel()) }
            launch {
                val budget = budgetInteractor.get().first()
                var budgetSum = budget.sum
                budgetSum += currencyInteractor.toTargetCurrency(
                    if (transaction.type.isIncome()) transaction.sum.negate() else transaction.sum,
                    transaction.currencyCode,
                    currencyInteractor.getMainCurrency().code
                )
                budgetInteractor.update(budget.copy(sum = budgetSum))
            }
        }
    }

    override suspend fun removeAllByCategory(category: Category) {
        transactionRepo.getAllByCategory(category)
            .map { tr -> removeTransaction(tr.toDomainModel(category)) }
    }

    override suspend fun clearAll() {
        transactionRepo.clearAll()
    }

    companion object {
        private const val PERCENT_FULL_COUNT = 100
    }
}
