package com.d9tilov.android.transaction.domain.impl

import androidx.paging.PagingData
import androidx.paging.map
import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.category.domain.entity.exception.CategoryException
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.model.Currency
import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.model.isIncome
import com.d9tilov.android.core.model.negate
import com.d9tilov.android.core.utils.countDaysRemainingNextFiscalDate
import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.divideBy
import com.d9tilov.android.core.utils.getEndDateOfFiscalPeriod
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.core.utils.getStartDateOfFiscalPeriod
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.core.utils.roundToCurrency
import com.d9tilov.android.core.utils.sumOfDecimals
import com.d9tilov.android.core.utils.toDecimal
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.contract.TransactionRepo
import com.d9tilov.android.transaction.domain.impl.mapper.toChartModel
import com.d9tilov.android.transaction.domain.impl.mapper.toDataModel
import com.d9tilov.android.transaction.domain.impl.mapper.toDomainModel
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.domain.model.TransactionChartModel
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import com.d9tilov.android.transaction.domain.model.TransactionMinMaxDateModel
import com.d9tilov.android.transaction.domain.model.TransactionSpendingTodayModel
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.transaction.regular.domain.model.RegularTransaction
import com.d9tilov.android.user.domain.contract.UserInteractor
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus

@Inject
class TransactionInteractorImpl(
    private val transactionRepo: TransactionRepo,
    private val regularTransactionInteractor: RegularTransactionInteractor,
    private val categoryInteractor: CategoryInteractor,
    private val userInteractor: UserInteractor,
    private val currencyInteractor: CurrencyInteractor,
    private val budgetInteractor: BudgetInteractor,
) : TransactionInteractor {
    override suspend fun addTransaction(transaction: Transaction) {
        val currencyCode = transaction.sum.currency.name
        val amount = transaction.sum.toDecimal()
        val usdSumValue = currencyInteractor.toUsd(amount, currencyCode)
        val newTransaction =
            transaction.copy(usdSum = usdSumValue).toDataModel()
        coroutineScope {
            launch { transactionRepo.addTransaction(newTransaction) }
            launch {
                val category = categoryInteractor.getCategoryById(transaction.category.id)
                val count = category.usageCount + 1
                categoryInteractor.update(category.copy(usageCount = count))
            }
            launch {
                val budget = checkNotNull(budgetInteractor.get().firstOrNull())
                var budgetSum = budget.sum
                budgetSum +=
                    currencyInteractor.toTargetCurrency(
                        if (transaction.type.isIncome()) amount else amount.negate(),
                        currencyCode,
                        currencyInteractor.getMainCurrency().code,
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
        onlySubcategories: Boolean,
    ): Flow<List<TransactionChartModel>> =
        categoryInteractor
            .getGroupedCategoriesByType(type)
            .flatMapLatest { categoryList ->
                val categoriesById = categoryList.associateBy { category -> category.id }
                val parentChildrenMap: Map<Category, List<Category>> =
                    categoryList.groupBy { category -> category.parent ?: category }
                transactionRepo
                    .getTransactionsByTypeInPeriod(from, to, type, inStatistics)
                    .map {
                        it.map { item: TransactionDataModel ->
                            val category =
                                categoriesById[item.categoryId]
                                    ?: throw CategoryException.CategoryNotFoundException(
                                        "getTransactionsGroupedByCategory Not found category with id: " +
                                            item.categoryId,
                                    )
                            item.toChartModel(
                                category,
                                currencyCode,
                                getItemSumBaseOnCurrency(currencyCode, item),
                            )
                        }
                    }.map { list ->
                        val sum = list.sumOfDecimals { tr -> tr.sum }
                        list
                            .groupBy { tr ->
                                if (onlySubcategories) {
                                    tr.category
                                } else {
                                    tr.category.parent ?: tr.category
                                }
                            }.mapKeys { entry ->
                                val category = entry.key
                                category.copy(children = parentChildrenMap[category] ?: emptyList())
                            }.map { entry: Map.Entry<Category, List<TransactionChartModel>> ->
                                val currencySum: Decimal = entry.value.sumOfDecimals { item -> item.sum }
                                val transaction: TransactionChartModel =
                                    checkNotNull(entry.value.firstOrNull())
                                val category = entry.key
                                TransactionChartModel(
                                    transaction.clientId,
                                    transaction.type,
                                    category,
                                    currencyCode,
                                    currencySum,
                                    currencySum
                                        .divideBy(sum)
                                        .times(Decimal(PERCENT_FULL_COUNT)),
                                )
                            }
                    }
            }

    private suspend fun getItemSumBaseOnCurrency(
        currencyCode: String,
        item: TransactionDataModel,
    ): Decimal =
        if (currencyCode == DEFAULT_CURRENCY_CODE) {
            item.usdSum
        } else {
            if (currencyCode == item.currencyCode) {
                item.sum
            } else {
                val trCurrency = currencyInteractor.getCurrencyByCode(currencyCode)
                trCurrency.value.times(item.usdSum)
            }
        }

    override suspend fun getTransactionsByCategoryId(
        categoryId: Long,
        from: LocalDateTime,
        to: LocalDateTime,
        inStatistics: Boolean,
    ): List<Transaction> {
        val category = categoryInteractor.getCategoryById(categoryId)
        val categoryList: List<Category> = category.children.ifEmpty { listOf(category) }
        val categoriesById = categoryList.associateBy { item -> item.id }
        return categoryList.flatMap { item: Category ->
            transactionRepo
                .getByCategoryInPeriod(item, from, to, inStatistics)
                .firstOrNull()
                ?.map { tr: TransactionDataModel ->
                    val foundCategory =
                        categoriesById[tr.categoryId]
                            ?: throw CategoryException.CategoryNotFoundException(
                                "getTransactionsByCategory Not found category with id: ${tr.categoryId}",
                            )
                    tr.toDomainModel(foundCategory)
                } ?: emptyList()
        }
    }

    override fun getTransactionById(id: Long): Flow<Transaction> =
        transactionRepo
            .getTransactionById(id)
            .map { transactionDataModel ->
                val category = categoryInteractor.getCategoryById(transactionDataModel.categoryId)
                transactionDataModel.toDomainModel(category, roundUsd = false)
            }

    override fun getTransactionsByType(type: TransactionType): Flow<PagingData<Transaction>> =
        transactionsWithCategories(type, transactionRepo.getTransactionsByType(type))

    override fun searchTransactionsByType(
        type: TransactionType,
        query: String,
    ): Flow<PagingData<Transaction>> =
        transactionsWithCategories(type, transactionRepo.searchTransactionsByType(type, query))

    private fun transactionsWithCategories(
        type: TransactionType,
        transactions: Flow<PagingData<TransactionDataModel>>,
    ): Flow<PagingData<Transaction>> =
        categoryInteractor
            .getGroupedCategoriesByType(type)
            .flatMapLatest { categoryList ->
                val categoriesById = categoryList.associateBy { category -> category.id }
                transactions
                    .map {
                        it.map { item ->
                            val category =
                                categoriesById[item.categoryId]
                                    ?: throw CategoryException.CategoryNotFoundException(
                                        "getTransactionsByType Not found category with id: ${item.categoryId}",
                                    )
                            item.toDomainModel(category)
                        }
                    }
            }

    override fun ableToSpendToday(): Flow<TransactionSpendingTodayModel> {
        val countDaysSinceFiscalDateFlow: Flow<Decimal> =
            flow { emit(userInteractor.getFiscalDay()) }.map { fiscalDay ->
                currentDateTime().countDaysRemainingNextFiscalDate(fiscalDay).toDecimal()
            }
        val numeratorFlow: Flow<Decimal> = getNumerator()
        val expensesPerCurrentDayFlow = getExpensesPerCurrentDay()

        return combine(
            numeratorFlow,
            countDaysSinceFiscalDateFlow,
            expensesPerCurrentDayFlow,
        ) { numerator, countDaysSinceFiscalDate, expensesPerCurrentDay ->
            val dailyAvailable =
                numerator
                    .divideBy(countDaysSinceFiscalDate)
                    .minus(expensesPerCurrentDay)
                    .roundToCurrency(currencyInteractor.getMainCurrency().code)
            if (dailyAvailable.isNegative) {
                TransactionSpendingTodayModel.OVERSPENDING(dailyAvailable)
            } else {
                TransactionSpendingTodayModel.NORMAL(dailyAvailable)
            }
        }
    }

    private fun getExpensesPerCurrentDay(): Flow<Decimal> =
        transactionRepo
            .getTransactionsByTypeInPeriod(
                currentDateTime().getStartOfDay(),
                currentDateTime().getEndOfDay(),
                TransactionType.EXPENSE,
                onlyInStatistics = true,
                withRegular = false,
            ).map { list ->
                sumAtCurrentRates(
                    items = list,
                    targetCurrencyCode = currencyInteractor.getMainCurrency().code,
                    amount = TransactionDataModel::sum,
                    currencyCode = TransactionDataModel::currencyCode,
                )
            }

    private fun getNumerator(): Flow<Decimal> {
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
                    regularTransactionInteractor
                        .getAll(TransactionType.EXPENSE)
                        .map { expenses -> getSumOfRegularTransactions(fiscalDay, expenses) }
                }
        val incomesFlow =
            flow { emit(userInteractor.getFiscalDay()) }
                .flatMapMerge { fiscalDay ->
                    val endDate = currentDateTime().getEndOfDay()
                    val startDate = getStartDateOfFiscalPeriod(fiscalDay)
                    transactionRepo
                        .getTransactionsByTypeInPeriod(
                            startDate,
                            endDate,
                            TransactionType.INCOME,
                            onlyInStatistics = true,
                            withRegular = false,
                        ).map { list ->
                            sumAtCurrentRates(
                                items = list,
                                targetCurrencyCode = currencyInteractor.getMainCurrency().code,
                                amount = TransactionDataModel::sum,
                                currencyCode = TransactionDataModel::currencyCode,
                            )
                        }
                }
        val expenseFlow =
            flow { emit(userInteractor.getFiscalDay()) }
                .flatMapMerge { fiscalDay ->
                    val endDate = currentDateTime()
                    val endDateMinusDay =
                        endDate.date
                            .minus(1, DateTimeUnit.DAY)
                            .atTime(0, 0, 0, 0)
                            .getEndOfDay()
                    val startDate = getStartDateOfFiscalPeriod(fiscalDay)
                    transactionRepo
                        .getTransactionsByTypeInPeriod(
                            startDate,
                            endDateMinusDay,
                            TransactionType.EXPENSE,
                            onlyInStatistics = true,
                            withRegular = false,
                        ).map { list ->
                            sumAtCurrentRates(
                                items = list,
                                targetCurrencyCode = currencyInteractor.getMainCurrency().code,
                                amount = TransactionDataModel::sum,
                                currencyCode = TransactionDataModel::currencyCode,
                            )
                        }
                }

        val savedSumPerPeriodFlow = budgetInteractor.get().map { it.saveSum }

        return combine(
            regularIncomeSumFlow,
            regularExpenseSumFlow,
            savedSumPerPeriodFlow,
            incomesFlow,
            expenseFlow,
        ) { regularIncomes, regularExpenses, savedSumPerPeriod, incomes, expenses ->
            regularIncomes
                .minus(regularExpenses)
                .minus(savedSumPerPeriod)
                .plus(incomes)
                .minus(expenses)
        }
    }

    private suspend fun getSumOfRegularTransactions(
        fiscalDay: Int,
        transactions: List<RegularTransaction>,
    ): Decimal =
        sumAtCurrentRates(
            items = transactions,
            targetCurrencyCode = currencyInteractor.getMainCurrency().code,
            amount = { transaction ->
                transaction.amountInPeriod(fiscalDay)
            },
            currencyCode = RegularTransaction::currencyCode,
        )

    private fun RegularTransaction.amountInPeriod(fiscalDay: Int): Decimal {
        val curDate = currentDateTime().getEndOfDay()
        val startDate = getStartDateOfFiscalPeriod(fiscalDay).date
        val endDate = curDate.getEndDateOfFiscalPeriod(fiscalDay).date
        return if (executionPeriod is ExecutionPeriod.EveryYear) {
            annualAmountInPeriod(maxOf(createdDate.date, startDate, curDate.date), endDate)
        } else {
            val occurrenceCount = occurrencesBetween(executionPeriod, createdDate.date, startDate, endDate).size
            sum.times(Decimal(occurrenceCount))
        }
    }

    private fun RegularTransaction.annualAmountInPeriod(
        from: LocalDate,
        to: LocalDate,
    ): Decimal {
        if (from > to) return Decimal.ZERO

        return (from.year..to.year).sumOfDecimals { year ->
            val yearStart = LocalDate(year, 1, 1)
            val nextYearStart = LocalDate(year + 1, 1, 1)
            val periodStart = maxOf(from, yearStart)
            val periodEnd = minOf(to.plus(1, DateTimeUnit.DAY), nextYearStart)
            sum
                .times(periodStart.daysUntil(periodEnd).toDecimal())
                .divideBy(yearStart.daysUntil(nextYearStart).toDecimal())
        }
    }

    override fun getSumInPeriod(
        from: LocalDateTime,
        to: LocalDateTime,
        transactionType: TransactionType,
        currencyCode: String,
        inStatistics: Boolean,
    ): Flow<Decimal> =
        transactionRepo
            .getTransactionsByTypeInPeriod(
                from,
                to,
                transactionType,
                onlyInStatistics = inStatistics,
                withRegular = true,
            ).map { list -> list.sumStoredValuesInCurrency(currencyCode) }

    override fun getApproxSumInFiscalPeriodCurrentCurrency(type: TransactionType): Flow<Decimal> =
        currencyInteractor
            .getMainCurrencyFlow()
            .flatMapMerge { currency ->
                val fiscalDay = userInteractor.getFiscalDay()
                val endDate = currentDateTime().getEndOfDay()
                val startDate = getStartDateOfFiscalPeriod(fiscalDay)
                transactionRepo
                    .getTransactionsByTypeInPeriod(
                        startDate,
                        endDate,
                        type,
                    ).map { list -> list.sumStoredValuesInCurrency(currency.code).roundToCurrency(currency.code) }
            }

    override fun getApproxSumTodayCurrentCurrency(type: TransactionType): Flow<Decimal> =
        combine(
            transactionRepo.getTransactionsByTypeInPeriod(
                currentDateTime().getStartOfDay(),
                currentDateTime().getEndOfDay(),
                type,
            ),
            currencyInteractor.getMainCurrencyFlow(),
        ) { list, currency ->
            list.sumStoredValuesInCurrency(currency.code).roundToCurrency(currency.code)
        }

    private suspend fun List<TransactionDataModel>.sumStoredValuesInCurrency(targetCurrencyCode: String): Decimal {
        val targetCurrencyRate =
            if (any { transaction -> transaction.currencyCode != targetCurrencyCode }) {
                currencyInteractor.getCurrencyByCode(targetCurrencyCode).value
            } else {
                null
            }
        return sumOfDecimals { transaction ->
            if (transaction.currencyCode == targetCurrencyCode) {
                transaction.sum
            } else {
                checkNotNull(targetCurrencyRate).times(transaction.usdSum)
            }
        }
    }

    private suspend fun <T> sumAtCurrentRates(
        items: List<T>,
        targetCurrencyCode: String,
        amount: (T) -> Decimal,
        currencyCode: (T) -> String,
    ): Decimal {
        val totalsByCurrency = items.groupingBy(currencyCode).fold(Decimal.ZERO) { total, item -> total + amount(item) }
        val targetAmount = totalsByCurrency[targetCurrencyCode] ?: Decimal.ZERO
        val sourceTotals = totalsByCurrency.filterKeys { it != targetCurrencyCode }
        if (sourceTotals.isEmpty()) return targetAmount

        val targetRate = currencyInteractor.getCurrencyByCode(targetCurrencyCode).value
        var numerator = Decimal.ZERO
        var denominator = Decimal.ONE
        // Add each source total / rate exactly so the combined amount can be rounded once.
        for ((code, subtotal) in sourceTotals) {
            val sourceRate = currencyInteractor.getCurrencyByCode(code).value
            numerator = numerator * sourceRate + subtotal * denominator
            denominator *= sourceRate
        }
        val precision = Currency.valueOf(targetCurrencyCode).precision
        return (targetAmount * denominator + numerator * targetRate).divide(denominator, precision)
    }

    override suspend fun getTransactionMinMaxDate(): TransactionMinMaxDateModel =
        transactionRepo.getTransactionMinMaxDate()

    override suspend fun executeRegularIfNeeded(type: TransactionType): List<RegularTransaction> {
        val addedTransactions = mutableListOf<RegularTransaction>()
        regularTransactionInteractor
            .getAll(type)
            .map { transactions ->
                for (tr in transactions) {
                    val amount = Currency.fromCode(tr.currencyCode)?.let(tr.sum::toMoney) ?: continue
                    val curDay = currentDate()
                    val skippedDates =
                        occurrencesBetween(
                            executionPeriod = tr.executionPeriod,
                            activeFrom = tr.createdDate.date,
                            from =
                                tr.executionPeriod.lastExecutionDateTime.date.plus(
                                    1,
                                    DateTimeUnit.DAY,
                                ),
                            to = curDay,
                        )
                    for (day in skippedDates) {
                        addTransaction(
                            Transaction.EMPTY.copy(
                                type = tr.type,
                                sum = amount,
                                category = tr.category,
                                date = day.getStartOfDay(),
                                description = tr.description,
                                isRegular = true,
                                inStatistics = true,
                            ),
                        )
                        addedTransactions.add(tr)
                    }
                    skippedDates.lastOrNull()?.let { lastExecutionDate ->
                        regularTransactionInteractor.update(
                            tr.copy(
                                executionPeriod =
                                    tr.executionPeriod.withLastExecutionDate(lastExecutionDate),
                            ),
                        )
                    }
                }
            }.firstOrNull()
        return addedTransactions
    }

    private fun ExecutionPeriod.withLastExecutionDate(date: LocalDate): ExecutionPeriod =
        when (this) {
            is ExecutionPeriod.EveryDay -> ExecutionPeriod.EveryDay(date.getStartOfDay())
            is ExecutionPeriod.EveryWeek -> ExecutionPeriod.EveryWeek(dayOfWeek, date.getStartOfDay())
            is ExecutionPeriod.EveryMonth -> ExecutionPeriod.EveryMonth(dayOfMonth, date.getStartOfDay())
            is ExecutionPeriod.EveryYear -> ExecutionPeriod.EveryYear(month, dayOfMonth, date.getStartOfDay())
        }

    override suspend fun update(transaction: Transaction) {
        val oldTransaction =
            checkNotNull(transactionRepo.getTransactionById(transaction.id).firstOrNull())
        val amount = transaction.sum.toDecimal()
        val usdSumValue = currencyInteractor.toUsd(amount, transaction.sum.currency.name)
        val budget = checkNotNull(budgetInteractor.get().firstOrNull())
        val mainCurrencyCode = currencyInteractor.getMainCurrency().code
        var budgetSum = budget.sum
        budgetSum +=
            currencyInteractor.toTargetCurrency(
                if (oldTransaction.type.isIncome()) oldTransaction.sum.negate() else oldTransaction.sum,
                oldTransaction.currencyCode,
                mainCurrencyCode,
            )
        budgetSum +=
            currencyInteractor.toTargetCurrency(
                if (transaction.type.isIncome()) amount else amount.negate(),
                transaction.sum.currency.name,
                mainCurrencyCode,
            )

        transactionRepo.update(transaction.toDataModel().copy(usdSum = usdSumValue))
        budgetInteractor.update(budget.copy(sum = budgetSum))
    }

    override suspend fun removeTransaction(transaction: Transaction) {
        val amount = transaction.sum.toDecimal()
        coroutineScope {
            launch { transactionRepo.removeTransaction(transaction.toDataModel()) }
            launch {
                val budget = checkNotNull(budgetInteractor.get().firstOrNull())
                var budgetSum = budget.sum
                budgetSum +=
                    currencyInteractor.toTargetCurrency(
                        if (transaction.type.isIncome()) amount.negate() else amount,
                        transaction.sum.currency.name,
                        currencyInteractor.getMainCurrency().code,
                    )
                budgetInteractor.update(budget.copy(sum = budgetSum))
            }
        }
    }

    override suspend fun removeAllByCategory(category: Category) {
        transactionRepo
            .getAllByCategory(category)
            .forEach { tr -> removeTransaction(tr.toDomainModel(category)) }
    }

    companion object {
        private const val PERCENT_FULL_COUNT = 100
    }
}
