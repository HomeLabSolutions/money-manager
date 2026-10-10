package com.d9tilov.android.insights.data.impl

import com.d9tilov.android.insights.data.contract.InsightsAggregator
import com.d9tilov.android.insights.data.contract.model.DailyTotalDto
import com.d9tilov.android.insights.domain.model.CategoryName
import com.d9tilov.android.insights.domain.model.CategoryTrace
import com.d9tilov.android.insights.domain.model.DailyTotal
import com.d9tilov.android.insights.domain.model.InsightTransaction
import javax.inject.Inject

class DefaultInsightsAggregator @Inject constructor() : InsightsAggregator {
    override fun aggregateTransactions(rows: List<InsightTransaction>): Map<CategoryName, List<DailyTotalDto>> =
        rows
            .groupBy(InsightTransaction::category)
            .mapValues { (_, transactions) ->
                val dailyTotals =
                    transactions
                        .groupBy { Triple(it.date.date, it.type, it.currency) }
                        .map { (key, dayTransactions) ->
                            DailyTotal(
                                date = key.first,
                                type = key.second,
                                currency = key.third,
                                amount = dayTransactions.sumOf(InsightTransaction::amount),
                            )
                        }.sortedWith(compareBy(DailyTotal::date, { it.type.value }, DailyTotal::currency))
                CategoryTrace(dailyTotals)
            }.toDto()

    private fun Map<CategoryName, CategoryTrace>.toDto(): Map<CategoryName, List<DailyTotalDto>> =
        mapValues { (_, trace) ->
            trace.days.map { day ->
                DailyTotalDto(
                    date = day.date.toString(),
                    type = day.type.name,
                    currency = day.currency,
                    amount = day.amount.stripTrailingZeros().toPlainString(),
                )
            }
        }
}
