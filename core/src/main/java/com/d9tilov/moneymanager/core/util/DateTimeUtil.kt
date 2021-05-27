package com.d9tilov.moneymanager.core.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs

const val RECENT_DATE_FORMAT = "dd.MM.yyyy"
const val TRANSACTION_DATE_FORMAT = "dd MMMM yyyy"
const val TRANSACTION_DATE_FORMAT_DAY_MONTH = "dd MMMM"
const val BACKUP_DATE = "dd.MM.yyyy HH:mm"

fun formatDate(date: Date, format: String): String {
    val now = Date(System.currentTimeMillis()).getStartOfDay().time
    val diff = now - date.getStartOfDay().time
    if (abs(diff) < DateUtils.DAY_IN_MILLIS * 2) {
        return DateUtils.getRelativeTimeSpanString(date.time, now, DateUtils.DAY_IN_MILLIS)
            .toString()
    }
    return SimpleDateFormat(format, Locale.getDefault()).format(date)
}

/**
 * Checks if two dates are on the same day ignoring time.
 * @param otherDate the second date, not altered, not null
 * @return true if they represent the same day
 */
fun Date.isSameDay(otherDate: Date): Boolean {
    val cal1: Calendar = Calendar.getInstance()
    cal1.time = this
    val cal2: Calendar = Calendar.getInstance()
    cal2.time = otherDate
    return cal1.isSameDay(cal2)
}

/**
 * Checks if two calendars represent the same day ignoring time.
 * @param otherCal the second calendar, not altered, not null
 * @return true if they represent the same day
 */
fun Calendar.isSameDay(otherCal: Calendar): Boolean {
    return get(Calendar.ERA) == otherCal.get(Calendar.ERA) &&
        get(Calendar.YEAR) == otherCal.get(Calendar.YEAR) &&
        get(Calendar.DAY_OF_YEAR) == otherCal.get(Calendar.DAY_OF_YEAR)
}

/**
 * Checks if a date is today.
 * @return true if the date is today.
 */
fun Date.isToday(): Boolean {
    return isSameDay(Calendar.getInstance().time)
}

/**
 * Checks if a calendar date is today.
 * @return true if cal date is today
 */
fun Calendar.isToday(): Boolean {
    return isSameDay(Calendar.getInstance())
}

fun Date.getStartOfDay(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val day = calendar[Calendar.DATE]
    calendar[year, month, day, 0, 0] = 0
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time
}

fun Date.getEndOfDay(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val day = calendar[Calendar.DATE]
    calendar[year, month, day, 23, 59] = 59
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar.time
}

fun Date.getStartDateOfFiscalDayAndCurrent(fiscalDay: Int): Date {
    val c = Calendar.getInstance()
    val dayOfMonth = c.get(Calendar.DAY_OF_MONTH)
    val curDate = Date()
    val fiscalDate: Date
    when {
        dayOfMonth == fiscalDay -> fiscalDate = curDate
        dayOfMonth > fiscalDay -> {
            c.add(Calendar.DATE, (fiscalDay - dayOfMonth))
            fiscalDate = c.time
        }
        else -> {
            c.add(Calendar.MONTH, -1)
            val daysCountInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH)
            if (fiscalDay > dayOfMonth) {
                c.set(Calendar.DAY_OF_MONTH, daysCountInMonth)
            } else {
                c.set(Calendar.DAY_OF_MONTH, fiscalDay)
            }
            fiscalDate = c.time
        }
    }
    return fiscalDate.getStartOfDay()
}

fun getFirstDayOfMonth(): Date {
    val c = Calendar.getInstance()
    c[Calendar.DAY_OF_MONTH] = 1
    return c.time
}

fun Date.getDayOfWeek(): Int {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal[Calendar.DAY_OF_WEEK]
}

fun Long.toBackupDate(): String {
    return SimpleDateFormat(BACKUP_DATE, Locale.getDefault()).format(Date(this))
}

fun Date.toBudgetCreatedDate(): String {
    return SimpleDateFormat(RECENT_DATE_FORMAT, Locale.getDefault()).format(this.time)
}
