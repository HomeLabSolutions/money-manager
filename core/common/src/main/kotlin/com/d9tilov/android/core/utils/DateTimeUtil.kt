@file:Suppress("TooManyFunctions", "PackageNaming")

package com.d9tilov.android.core.utils

import com.d9tilov.android.core.constants.DataConstants.UNKNOWN_BACKUP_DATE
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.Instant

const val DATE_FORMAT = "dd.MM.yyyy"
const val BACKUP_DATE = "dd.MM.yyyy HH:mm"
private const val NANOSECONDS_MINUS_ONE = 999_999_999
private const val SECONDS_MINUS_ONE = 59
private const val HOURS_MINUS_ONE = 23
private const val MAX_FISCAL_DAY = 31

fun Long.toBackupDate(): String =
    if (this == UNKNOWN_BACKUP_DATE) {
        "-"
    } else {
        SimpleDateFormat(
            BACKUP_DATE,
            Locale.getDefault(),
        ).format(Date(this))
    }

fun LocalDateTime.toStandardStringDate(): String =
    SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date(this.toMillis()))

fun currentDateTime(): LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun currentDate(): LocalDate = currentDateTime().date

fun Instant.toLocal() = this.toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.toMillis(timeZone: TimeZone = TimeZone.currentSystemDefault()): Long =
    this.toInstant(timeZone).toEpochMilliseconds()

fun Long.toLocalDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime =
    Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)

fun Long.toLocalDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate = toLocalDateTime(timeZone).date

fun LocalDate.getStartOfDay(): LocalDateTime = atTime(0, 0, 0, 0)

fun LocalDateTime.getStartOfDay(): LocalDateTime = date.atTime(0, 0, 0, 0)

fun LocalDate.getEndOfDay(): LocalDateTime =
    atTime(HOURS_MINUS_ONE, SECONDS_MINUS_ONE, SECONDS_MINUS_ONE, NANOSECONDS_MINUS_ONE)

fun LocalDateTime.getEndOfDay(): LocalDateTime =
    date.atTime(HOURS_MINUS_ONE, SECONDS_MINUS_ONE, SECONDS_MINUS_ONE, NANOSECONDS_MINUS_ONE)

fun getStartDateOfFiscalPeriod(fiscalDay: Int): LocalDateTime = getStartDateOfFiscalPeriod(fiscalDay, currentDate())

internal fun getStartDateOfFiscalPeriod(
    fiscalDay: Int,
    currentDate: LocalDate,
): LocalDateTime {
    val currentMonthFiscalDate = currentDate.fiscalDate(fiscalDay)
    val startDate =
        if (currentDate >= currentMonthFiscalDate) {
            currentMonthFiscalDate
        } else {
            currentDate.fiscalDate(fiscalDay, monthOffset = -1)
        }
    return startDate.getStartOfDay()
}

fun LocalDateTime.getEndDateOfFiscalPeriod(fiscalDay: Int): LocalDateTime {
    val currentMonthFiscalDate = date.fiscalDate(fiscalDay)
    val nextFiscalDate =
        if (date < currentMonthFiscalDate) {
            currentMonthFiscalDate
        } else {
            date.fiscalDate(fiscalDay, monthOffset = 1)
        }
    return nextFiscalDate.minus(1, DateTimeUnit.DAY).getEndOfDay()
}

fun LocalDateTime.countDaysRemainingNextFiscalDate(fiscalDay: Int): Int {
    val currentMonthFiscalDate = date.fiscalDate(fiscalDay)
    val nextFiscalDate =
        if (date < currentMonthFiscalDate) {
            currentMonthFiscalDate
        } else {
            date.fiscalDate(fiscalDay, monthOffset = 1)
        }
    return date.daysUntil(nextFiscalDate)
}

private fun LocalDate.fiscalDate(
    fiscalDay: Int,
    monthOffset: Int = 0,
): LocalDate {
    require(fiscalDay in 1..MAX_FISCAL_DAY) { "Fiscal day must be between 1 and $MAX_FISCAL_DAY" }
    val firstDayOfTargetMonth =
        LocalDate(year, month, 1).plus(monthOffset, DateTimeUnit.MONTH)
    val lastDayOfTargetMonth =
        firstDayOfTargetMonth
            .plus(1, DateTimeUnit.MONTH)
            .minus(1, DateTimeUnit.DAY)
            .day
    return if (fiscalDay <= lastDayOfTargetMonth) {
        LocalDate(firstDayOfTargetMonth.year, firstDayOfTargetMonth.month, fiscalDay)
    } else {
        LocalDate(firstDayOfTargetMonth.year, firstDayOfTargetMonth.month, lastDayOfTargetMonth)
    }
}
