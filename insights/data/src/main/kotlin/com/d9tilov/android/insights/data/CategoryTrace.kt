package com.d9tilov.android.insights.data

import com.d9tilov.android.core.model.TransactionType
import kotlinx.datetime.LocalDate
import java.math.BigDecimal

internal typealias CategoryName = String

internal data class CategoryTrace(
    val days: List<DailyTotal>,
)

internal data class DailyTotal(
    val date: LocalDate,
    val type: TransactionType,
    val currency: String,
    val amount: BigDecimal,
) {
    fun toPayload(): Map<String, Any> =
        mapOf(
            "date" to date.toString(),
            "type" to type.name,
            "currency" to currency,
            "amount" to amount.stripTrailingZeros().toPlainString(),
        )
}

internal fun aggregateTransactions(rows: List<InsightTransaction>): Map<CategoryName, CategoryTrace> =
    rows.groupBy(InsightTransaction::category).mapValues { (_, transactions) ->
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
    }

internal fun Map<CategoryName, CategoryTrace>.toPayload(): Map<String, List<Map<String, Any>>> =
    mapValues { (_, trace) -> trace.days.map(DailyTotal::toPayload) }
