package com.d9tilov.android.transaction.domain.impl

import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.utils.divideBy
import com.d9tilov.android.transaction.regular.domain.model.RegularTransaction
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import java.math.BigDecimal

/** Annual amounts accrue evenly over each calendar year for the spending allowance. */
internal fun RegularTransaction.amountInPeriod(
    from: LocalDate,
    to: LocalDate,
    today: LocalDate,
): BigDecimal =
    if (executionPeriod is ExecutionPeriod.EveryYear) {
        annualAmountInPeriod(maxOf(createdDate.date, from, today), to)
    } else {
        val occurrenceCount = occurrencesBetween(executionPeriod, createdDate.date, from, to).size
        sum.multiply(BigDecimal(occurrenceCount))
    }

private fun RegularTransaction.annualAmountInPeriod(
    from: LocalDate,
    to: LocalDate,
): BigDecimal {
    if (from > to) return BigDecimal.ZERO

    return (from.year..to.year).sumOf { year ->
        val yearStart = LocalDate(year, 1, 1)
        val nextYearStart = LocalDate(year + 1, 1, 1)
        val periodStart = maxOf(from, yearStart)
        val periodEnd = minOf(to.plus(1, DateTimeUnit.DAY), nextYearStart)
        sum
            .multiply(periodStart.daysUntil(periodEnd).toBigDecimal())
            .divideBy(yearStart.daysUntil(nextYearStart).toBigDecimal())
    }
}
