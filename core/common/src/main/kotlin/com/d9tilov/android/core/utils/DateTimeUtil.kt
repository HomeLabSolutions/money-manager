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
import kotlinx.datetime.offsetAt
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

const val DATE_FORMAT = "dd.MM.yyyy"
const val BACKUP_DATE = "dd.MM.yyyy HH:mm"
private const val MILLISECONDS_IN_SECOND = 1000L
private const val MILLISECONDS_MINUS_ONE = 999
private const val SECONDS_MINUS_ONE = 59
private const val HOURS_MINUS_ONE = 23

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

@OptIn(ExperimentalTime::class)
fun currentDateTime(): LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun currentDate(): LocalDate = currentDateTime().date

@OptIn(ExperimentalTime::class)
fun LocalDateTime.toUTC(): LocalDateTime =
    Instant.fromEpochMilliseconds(this.toMillis()).toLocalDateTime(
        TimeZone.UTC,
    )

@OptIn(ExperimentalTime::class)
fun Instant.toLocal() = this.toLocalDateTime(TimeZone.currentSystemDefault())

@OptIn(ExperimentalTime::class)
fun LocalDateTime.toMillis(): Long = this.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

@OptIn(ExperimentalTime::class)
fun Long.toLocalDateTime(): LocalDateTime =
    Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault())

@OptIn(ExperimentalTime::class)
fun Long.fromUTCToLocalDateTime(): LocalDateTime {
    val timeZone = TimeZone.currentSystemDefault()
    val newMillis =
        this + timeZone.offsetAt(Instant.fromEpochMilliseconds(this)).totalSeconds * MILLISECONDS_IN_SECOND
    return Instant.fromEpochMilliseconds(newMillis).toLocal()
}

@OptIn(ExperimentalTime::class)
fun Long.toLocalDate(): LocalDate =
    Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault()).date

fun LocalDate.getStartOfDay(): LocalDateTime = atTime(0, 0, 0, 0)

fun LocalDateTime.getStartOfDay(): LocalDateTime = date.atTime(0, 0, 0, 0)

fun LocalDate.getEndOfDay(): LocalDateTime =
    atTime(HOURS_MINUS_ONE, SECONDS_MINUS_ONE, SECONDS_MINUS_ONE, MILLISECONDS_MINUS_ONE)

fun LocalDateTime.getEndOfDay(): LocalDateTime =
    date.atTime(HOURS_MINUS_ONE, SECONDS_MINUS_ONE, SECONDS_MINUS_ONE, MILLISECONDS_MINUS_ONE)

fun LocalDateTime.isSameDay(date: LocalDateTime): Boolean = this.year == date.year && this.dayOfYear == date.dayOfYear

fun LocalDate.isSameDay(date: LocalDate): Boolean = this.year == date.year && this.dayOfYear == date.dayOfYear

fun getStartDateOfFiscalPeriod(fiscalDay: Int): LocalDateTime {
    val c = Calendar.getInstance()
    val dayOfMonth = c.get(Calendar.DAY_OF_MONTH)
    val curDate = currentDate()
    val fiscalDate: LocalDate =
        when {
            dayOfMonth == fiscalDay -> curDate
            dayOfMonth > fiscalDay -> LocalDate(curDate.year, curDate.month, fiscalDay)
            else -> LocalDate(curDate.year, curDate.month, fiscalDay).minus(1, DateTimeUnit.MONTH)
        }
    return fiscalDate.getStartOfDay()
}

fun LocalDateTime.getEndDateOfFiscalPeriod(fiscalDay: Int): LocalDateTime {
    val c = currentDate()
    val fiscalDate: LocalDate =
        when {
            dayOfMonth == fiscalDay -> c.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
            dayOfMonth > fiscalDay ->
                LocalDate(c.year, c.month, fiscalDay)
                    .plus(1, DateTimeUnit.MONTH)
                    .minus(1, DateTimeUnit.DAY)
            else -> LocalDate(c.year, c.month, fiscalDay).minus(1, DateTimeUnit.DAY)
        }
    return fiscalDate.getEndOfDay()
}

fun LocalDateTime.countDaysRemainingNextFiscalDate(fiscalDay: Int): Int {
    val fiscalDate = LocalDate(this.year, this.month, fiscalDay)
    return if (this.date < fiscalDate) {
        this.date.daysUntil(fiscalDate)
    } else {
        this.date.daysUntil(fiscalDate.plus(1, DateTimeUnit.MONTH))
    }
}
