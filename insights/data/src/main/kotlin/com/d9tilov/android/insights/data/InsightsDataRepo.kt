package com.d9tilov.android.insights.data

import com.d9tilov.android.category.data.contract.CategorySource
import com.d9tilov.android.core.constants.DataConstants.DEFAULT_DATA_ID
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.core.utils.toMillis
import com.d9tilov.android.insights.data.mapper.toDomainModel
import com.d9tilov.android.insights.domain.GeneratedInsight
import com.d9tilov.android.insights.domain.Insight
import com.d9tilov.android.insights.domain.InsightTransaction
import com.d9tilov.android.insights.domain.InsightsConstants.INSIGHT_WINDOW_DAYS
import com.d9tilov.android.insights.domain.InsightsConstants.MIN_INSIGHT_HISTORY_DAYS
import com.d9tilov.android.insights.domain.InsightsRepository
import com.d9tilov.android.insights.domain.exception.DailyInsightLimitException
import com.d9tilov.android.insights.domain.exception.InsufficientInsightsDataException
import com.d9tilov.android.insights.domain.toDomainModel
import com.d9tilov.android.transaction.data.contract.TransactionSource
import com.d9tilov.android.user.domain.model.InsightLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsightsDataRepo @Inject constructor(
    private val localSource: InsightSource,
    private val insightLanguageSource: InsightLanguageSource,
    private val transactionSource: TransactionSource,
    private val categorySource: CategorySource,
    private val remoteSource: InsightsRemoteSource,
    private val aggregator: InsightsAggregator,
) : InsightsRepository {
    override fun history(): Flow<List<Insight>> =
        localSource.history().map { rows ->
            rows.map { it.toDomainModel() }
        }

    override suspend fun generate(): GeneratedInsight {
        if (localSource.isDailyLimitReached()) throw DailyInsightLimitException()
        return generateNew()
    }

    private suspend fun generateNew(): GeneratedInsight {
        val to = currentDateTime()
        val from = to.date.minus(INSIGHT_WINDOW_DAYS - 1, DateTimeUnit.DAY).getStartOfDay()
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
        val traces = aggregator.aggregateTransactions(rows)
        val previous = localSource.history().first().map { it.text }

        val selectedLanguage = insightLanguageSource.language.first()
        val languageTag =
            if (selectedLanguage == InsightLanguage.SYSTEM) {
                Locale.getDefault().toLanguageTag()
            } else {
                selectedLanguage.tag
            }
        val insight =
            remoteSource
                .generate(
                    periodStart = from.date.toString(),
                    periodEnd = to.date.toString(),
                    languageTag = languageTag,
                    traces = traces,
                    previousInsights = previous,
                ).toDomainModel()
        localSource.save(
            Insight(
                id = DEFAULT_DATA_ID,
                createdAtMillis = currentDateTime().toMillis(),
                text = insight.text,
            ),
        )
        return insight
    }

    internal companion object {
        internal fun hasTwoWeeksOfHistory(dates: List<LocalDate>): Boolean {
            val first = dates.minOrNull() ?: return false
            val last = dates.maxOrNull() ?: return false
            return first.daysUntil(last) >= MIN_INSIGHT_HISTORY_DAYS
        }
    }
}
