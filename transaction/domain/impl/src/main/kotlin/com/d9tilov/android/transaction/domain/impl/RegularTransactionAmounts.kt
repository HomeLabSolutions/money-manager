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
): BigDecimal =
    if (executionPeriod is ExecutionPeriod.EveryYear) {
        annualAmountInPeriod(maxOf(createdDate.date, from), to)
    } else {
        val occurrenceCount = occurrencesBetween(executionPeriod, createdDate.date, from, to).size
        sum.multiply(BigDecimal(occurrenceCount))
    }

private fun RegularTransaction.annualAmountInPeriod(
    from: LocalDate,
    to: LocalDate,
): BigDecimal {
    var start = from
    var total = BigDecimal.ZERO
    while (start <= to) {
        val yearStart = LocalDate(start.year, 1, 1)
        val nextYearStart = LocalDate(start.year + 1, 1, 1)
        val endExclusive = minOf(to.plus(1, DateTimeUnit.DAY), nextYearStart)
        total +=
            sum
                .multiply(start.daysUntil(endExclusive).toBigDecimal())
                .divideBy(yearStart.daysUntil(nextYearStart).toBigDecimal())
        start = endExclusive
    }
    return total
}
