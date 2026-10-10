package com.d9tilov.android.insights.domain.impl

import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.insights.domain.contract.InsightLanguageRepo
import com.d9tilov.android.insights.domain.contract.InsightsConsentRepo
import com.d9tilov.android.insights.domain.contract.InsightsInteractor
import com.d9tilov.android.insights.domain.contract.InsightsRepo
import com.d9tilov.android.insights.domain.model.GeneratedInsight
import com.d9tilov.android.insights.domain.model.Insight
import com.d9tilov.android.insights.domain.model.InsightsConstants.MIN_INSIGHT_TRANSACTION_COUNT
import com.d9tilov.android.transaction.domain.contract.TransactionRepo
import com.d9tilov.android.user.domain.model.InsightLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class InsightsInteractorImpl @Inject constructor(
    private val transactionRepo: TransactionRepo,
    private val insightsRepo: InsightsRepo,
    private val consentRepo: InsightsConsentRepo,
    private val languageRepo: InsightLanguageRepo,
) : InsightsInteractor {
    override val language: Flow<InsightLanguage>
        get() = languageRepo.language

    override suspend fun setLanguage(language: InsightLanguage) = languageRepo.setLanguage(language)

    override fun history(): Flow<List<Insight>> = insightsRepo.history()

    override suspend fun generate(): GeneratedInsight = insightsRepo.generate()

    override suspend fun isConsentGranted(): Boolean = consentRepo.isGranted.first()

    override suspend fun grantConsent() = consentRepo.grant()

    override fun hasEnoughTransactionsForInsight(): Flow<Boolean> {
        val (from, to) = insightsRepo.insightPeriod()
        val end = to.date.getEndOfDay()
        return combine(
            flow =
                transactionRepo.getTransactionsByTypeInPeriod(
                    from = from,
                    to = end,
                    transactionType = TransactionType.INCOME,
                    onlyInStatistics = true,
                    withRegular = true,
                ),
            flow2 =
                transactionRepo.getTransactionsByTypeInPeriod(
                    from = from,
                    to = end,
                    transactionType = TransactionType.EXPENSE,
                    onlyInStatistics = true,
                    withRegular = true,
                ),
        ) { income, expense ->
            val until = minOf(end, currentDateTime())
            income.count { it.date <= until } + expense.count { it.date <= until } >= MIN_INSIGHT_TRANSACTION_COUNT
        }
    }
}
