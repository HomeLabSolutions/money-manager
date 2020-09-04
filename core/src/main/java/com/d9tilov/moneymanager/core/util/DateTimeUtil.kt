package com.d9tilov.moneymanager.core.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs

private const val DAY_OF_WEEK_DATE_FORMAT = "EE. d MMMM"
const val RECENT_DATE_FORMAT = "dd.MM.yyyy"
const val TRANSACTION_DATE_FORMAT = "dd MMMM yyyy"
const val TRANSACTION_DATE_FORMAT_DAY_MONTH = "dd MMMM"

fun formatMinSec(time: Long): String {
    val seconds = (time / 1000).toInt()
    val min = seconds / 60
    return "$min:${String.format("%02d", seconds - min * 60)}"
}

fun formatDayOfWeekDate(date: Date): String {
    return SimpleDateFormat(DAY_OF_WEEK_DATE_FORMAT, Locale.getDefault()).format(date)
}

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

fun Date.getFirstDayOfMonth(): Date {
    val c = Calendar.getInstance()
    c[Calendar.DAY_OF_MONTH] = 1
    return c.time
}

fun Date.getDayOfWeek(): Int {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal[Calendar.DAY_OF_WEEK]
}
