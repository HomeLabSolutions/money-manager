package com.d9tilov.android.insights.data

import com.d9tilov.android.insights.domain.CategoryName
import com.d9tilov.android.insights.domain.DailyTotalDto
import com.d9tilov.android.insights.domain.InsightTransaction

interface InsightsAggregator {
    fun aggregateTransactions(rows: List<InsightTransaction>): Map<CategoryName, List<DailyTotalDto>>
}
