package com.d9tilov.android.insights.data

import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil

private const val MIN_INSIGHT_HISTORY_DAYS = 14

internal fun hasTwoWeeksOfHistory(dates: List<LocalDate>): Boolean {
    val first = dates.minOrNull() ?: return false
    val last = dates.maxOrNull() ?: return false
    return first.daysUntil(last) >= MIN_INSIGHT_HISTORY_DAYS
}
