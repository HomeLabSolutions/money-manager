package com.d9tilov.android.insights.data

import com.d9tilov.android.category.data.contract.CategorySource
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.insights.domain.Insight
import com.d9tilov.android.insights.domain.InsightLanguageRepository
import com.d9tilov.android.insights.domain.InsightsRepository
import com.d9tilov.android.insights.domain.InsufficientInsightsDataException
import com.d9tilov.android.insights.domain.NoNewInsightException
import com.d9tilov.android.transaction.data.contract.TransactionSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsightsDataRepo @Inject constructor(
    private val preferencesStore: PreferencesStore,
    private val localSource: InsightLocalSource,
    private val insightLanguageRepository: InsightLanguageRepository,
    private val transactionSource: TransactionSource,
    private val categorySource: CategorySource,
    private val remoteSource: InsightsRemoteSource,
) : InsightsRepository {
    private val generationMutex = Mutex()

    override fun history(): Flow<List<Insight>> =
        preferencesStore.uid.filterNotNull().flatMapLatest { clientId ->
            localSource.history(clientId).map { rows ->
                rows.map { Insight(id = it.id, createdAtMillis = it.createdAtMillis, text = it.text) }
            }
        }

    override suspend fun generate(languageTag: String): String =
        generationMutex.withLock {
            val clientId = requireNotNull(preferencesStore.uid.first())
            generateNew(clientId, languageTag)
        }

    private suspend fun generateNew(
        clientId: String,
        languageTag: String,
    ): String {
        val from = currentDate().minus(INSIGHT_WINDOW_DAYS - 1, DateTimeUnit.DAY).getStartOfDay()
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
        val previous = localSource.previous(clientId)

        val selectedLanguage = insightLanguageRepository.language.first()
        val insight =
            remoteSource.generate(
                periodStart = from.date.toString(),
                periodEnd = to.date.toString(),
                languageTag = selectedLanguage.ifBlank { languageTag },
                traces = traces,
                previousInsights = previous,
            )
        check(insight.length <= MAX_INSIGHT_TEXT_LENGTH)
        if (matchesPreviousInsight(insight, previous)) {
            throw NoNewInsightException()
        }
        localSource.save(clientId, insight)
        return insight
    }
}
