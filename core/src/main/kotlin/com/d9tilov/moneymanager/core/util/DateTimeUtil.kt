package com.d9tilov.moneymanager.core.util

import android.text.format.DateUtils
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.offsetAt
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs

const val DATE_FORMAT = "dd.MM.yyyy"
const val TRANSACTION_DATE_FORMAT = "dd MMMM yyyy"
const val TRANSACTION_DATE_FORMAT_SHORT = "dd.MM.yy"
const val BACKUP_DATE = "dd.MM.yyyy HH:mm"

fun formatDate(date: LocalDateTime, format: String): String {
    val now = currentDateTime().getStartOfDay().toMillis()
    val diff = now - date.getStartOfDay().toMillis()
    if (abs(diff) < DateUtils.DAY_IN_MILLIS * 2) {
        return DateUtils.getRelativeTimeSpanString(date.toMillis(), now, DateUtils.DAY_IN_MILLIS)
            .toString()
    }
    return SimpleDateFormat(format, Locale.getDefault()).format(Date(date.toMillis()))
}

fun Long.toBackupDate(): String {
    return SimpleDateFormat(BACKUP_DATE, Locale.getDefault()).format(Date(this))
}

fun LocalDateTime.toBudgetCreatedDate(): String {
    return SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date(this.toMillis()))
}

fun currentDateTime(): LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
fun currentDate(): LocalDate = currentDateTime().date
fun LocalDateTime.toUTC(): LocalDateTime = Instant.fromEpochMilliseconds(this.toMillis()).toLocalDateTime(
    TimeZone.UTC)
fun Instant.toLocal() = this.toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.toMillis(): Long =
    this.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

fun Long.toLocalDateTime(): LocalDateTime = Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault())
fun Long.fromUTCToLocalDateTime(): LocalDateTime {
    val timeZone = TimeZone.currentSystemDefault()
    val newMillis = this + timeZone.offsetAt(Instant.fromEpochMilliseconds(this)).totalSeconds * 1000L
    return Instant.fromEpochMilliseconds(newMillis).toLocal()
}
fun Long.toLocalDate(): LocalDate = Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault()).date

fun LocalDate.getStartOfDay(): LocalDateTime = atTime(0, 0, 0, 0)

fun LocalDateTime.getStartOfDay(): LocalDateTime = date.atTime(0, 0, 0, 0)

fun LocalDate.getEndOfDay(): LocalDateTime = atTime(23, 59, 59, 999)

fun LocalDateTime.getEndOfDay(): LocalDateTime = date.atTime(23, 59, 59, 999)

fun LocalDateTime.isSameDay(date: LocalDateTime): Boolean {
    return this.year == date.year && this.dayOfYear == date.dayOfYear
}

fun LocalDate.isSameDay(date: LocalDate): Boolean {
    return this.year == date.year && this.dayOfYear == date.dayOfYear
}

fun LocalDateTime.getStartDateOfFiscalPeriod(fiscalDay: Int): LocalDateTime {
    val c = Calendar.getInstance()
    val dayOfMonth = c.get(Calendar.DAY_OF_MONTH)
    val curDate = currentDate()
    val fiscalDate: LocalDate = when {
        dayOfMonth == fiscalDay -> curDate
        dayOfMonth > fiscalDay -> LocalDate(curDate.year, curDate.month, fiscalDay)
        else -> LocalDate(curDate.year, curDate.month, fiscalDay).minus(1, DateTimeUnit.MONTH)
    }
    return fiscalDate.getStartOfDay()
}

fun LocalDateTime.getEndDateOfFiscalPeriod(fiscalDay: Int): LocalDateTime {
    val c = currentDate()
    val fiscalDate: LocalDate = when {
        dayOfMonth == fiscalDay -> c.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
        dayOfMonth > fiscalDay -> LocalDate(c.year, c.month, fiscalDay).plus(1, DateTimeUnit.MONTH)
            .minus(1, DateTimeUnit.DAY)
        else -> LocalDate(c.year, c.month, fiscalDay).minus(1, DateTimeUnit.DAY)
    }
    return fiscalDate.getEndOfDay()
}

fun LocalDateTime.countDaysRemainingNextFiscalDate(fiscalDay: Int): Int {
    val fiscalDate = LocalDate(this.year, this.month, fiscalDay)
    return if (this.date < fiscalDate) this.date.daysUntil(fiscalDate)
    else this.date.daysUntil(fiscalDate.plus(1, DateTimeUnit.MONTH))
}
