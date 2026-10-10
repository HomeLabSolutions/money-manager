package com.d9tilov.android.insights.data.contract

import com.d9tilov.android.insights.data.contract.model.DailyTotalDto
import com.d9tilov.android.insights.domain.model.CategoryName
import com.d9tilov.android.insights.domain.model.InsightTransaction

interface InsightsAggregator {
    fun aggregateTransactions(rows: List<InsightTransaction>): Map<CategoryName, List<DailyTotalDto>>
}
