package com.d9tilov.android.insights.data

import com.d9tilov.android.category.data.contract.CategorySource
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.insights.domain.InsightsRepository
import com.d9tilov.android.insights.domain.InsufficientInsightsDataException
import com.d9tilov.android.transaction.data.contract.TransactionSource
import kotlinx.coroutines.flow.first
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import javax.inject.Inject

class InsightsDataRepo @Inject constructor(
    private val transactionSource: TransactionSource,
    private val categorySource: CategorySource,
    private val remoteSource: InsightsRemoteSource,
) : InsightsRepository {
    override suspend fun generate(languageTag: String): String {
        val from = currentDate().minus(3, DateTimeUnit.MONTH).getStartOfDay()
        val to = currentDateTime()
        val income =
            transactionSource
                .getAllByTypeInPeriod(
                    from,
                    to,
                    TransactionType.INCOME,
                    onlyInStatistics = true,
                    withRegular = true,
                ).first()
        val expense =
            transactionSource
                .getAllByTypeInPeriod(
                    from,
                    to,
                    TransactionType.EXPENSE,
                    onlyInStatistics = true,
                    withRegular = true,
                ).first()
        val transactions = (income + expense).filter { it.date <= to }
        if (!hasTwoWeeksOfHistory(transactions.map { it.date.date })) {
            throw InsufficientInsightsDataException()
        }
        val categoryNames =
            (
                categorySource.getCategoriesByType(TransactionType.INCOME).first() +
                    categorySource.getCategoriesByType(TransactionType.EXPENSE).first()
            ).flatMap { listOf(it) + it.children }
                .associate { it.id to it.name }
        val rows =
            transactions.map { transaction ->
                InsightTransaction(
                    type = transaction.type,
                    date = transaction.date,
                    category = categoryNames[transaction.categoryId] ?: "Uncategorized",
                    currency = transaction.currencyCode,
                    amount = transaction.sum,
                )
            }
        val traces = aggregateTransactions(rows).toPayload()

        return remoteSource.generate(
            periodStart = from.date.toString(),
            periodEnd = to.date.toString(),
            languageTag = languageTag,
            traces = traces,
        )
    }
}
