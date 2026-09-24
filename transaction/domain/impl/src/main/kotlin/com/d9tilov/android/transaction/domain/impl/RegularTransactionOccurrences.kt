package com.d9tilov.android.transaction.domain.impl

import com.d9tilov.android.core.model.ExecutionPeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.plus

/** Returns all scheduled dates in the inclusive [from]..[to] interval. */
internal fun occurrencesBetween(
    executionPeriod: ExecutionPeriod,
    activeFrom: LocalDate,
    from: LocalDate,
    to: LocalDate,
): List<LocalDate> {
    var date = maxOf(activeFrom, from)
    if (date > to) return emptyList()

    return buildList {
        while (date <= to) {
            if (executionPeriod.occursOn(date)) add(date)
            date = date.plus(1, DateTimeUnit.DAY)
        }
    }
}

private fun ExecutionPeriod.occursOn(date: LocalDate): Boolean =
    when (this) {
        is ExecutionPeriod.EveryDay -> true
        is ExecutionPeriod.EveryWeek -> date.dayOfWeek.ordinal == dayOfWeek
        is ExecutionPeriod.EveryMonth -> date.day == minOf(dayOfMonth, date.month.days(date.year))
    }

private fun Month.days(year: Int): Int =
    when (this) {
        Month.FEBRUARY -> if (year.isLeapYear()) DAYS_IN_LEAP_FEBRUARY else DAYS_IN_FEBRUARY
        Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> DAYS_IN_SHORT_MONTH
        else -> DAYS_IN_LONG_MONTH
    }

private fun Int.isLeapYear(): Boolean =
    this % LEAP_YEAR_INTERVAL == 0 &&
        (this % YEARS_IN_CENTURY != 0 || this % YEARS_IN_GREGORIAN_CYCLE == 0)

private const val DAYS_IN_FEBRUARY = 28
private const val DAYS_IN_LEAP_FEBRUARY = 29
private const val DAYS_IN_SHORT_MONTH = 30
private const val DAYS_IN_LONG_MONTH = 31
private const val LEAP_YEAR_INTERVAL = 4
private const val YEARS_IN_CENTURY = 100
private const val YEARS_IN_GREGORIAN_CYCLE = 400
