package com.d9tilov.android.insights.data

import com.d9tilov.android.insights.domain.CategoryName
import com.d9tilov.android.insights.domain.CategoryTrace
import com.d9tilov.android.insights.domain.DailyTotal
import com.d9tilov.android.insights.domain.DailyTotalDto
import com.d9tilov.android.insights.domain.InsightTransaction
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
