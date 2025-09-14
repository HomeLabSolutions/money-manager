@file:Suppress("TooManyFunctions", "PackageNaming")

package com.d9tilov.android.common.android.utils

import android.text.format.DateUtils
import android.text.format.DateUtils.DAY_IN_MILLIS
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.core.utils.toMillis
import kotlinx.datetime.LocalDateTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

const val TRANSACTION_DATE_FORMAT = "dd MMMM yyyy"

fun formatDate(
    date: LocalDateTime,
    format: String,
): String {
    val now = currentDateTime().getStartOfDay().toMillis()
    val diff = now - date.getStartOfDay().toMillis()
    if (abs(diff) < DAY_IN_MILLIS * 2) {
        return DateUtils.getRelativeTimeSpanString(date.toMillis(), now, DAY_IN_MILLIS).toString()
    }
    return SimpleDateFormat(format, Locale.getDefault()).format(Date(date.toMillis()))
}
