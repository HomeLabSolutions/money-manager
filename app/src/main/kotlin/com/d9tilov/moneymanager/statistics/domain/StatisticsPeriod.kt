package com.d9tilov.moneymanager.statistics.domain

import android.os.Parcelable
import com.d9tilov.moneymanager.core.util.currentDate
import com.d9tilov.moneymanager.core.util.getEndOfDay
import com.d9tilov.moneymanager.core.util.getStartOfDay
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed class StatisticsPeriod(val name: String, val from: LocalDateTime, val to: LocalDateTime) :
    Parcelable {

    companion object {
        private const val STATISTICS_PERIOD_DAY_NAME = "statistics_period_day"
        private const val STATISTICS_PERIOD_WEEK_NAME = "statistics_period_week"
        private const val STATISTICS_PERIOD_MONTH_NAME = "statistics_period_month"
        private const val STATISTICS_PERIOD_YEAR_NAME = "statistics_period_year"
        private const val STATISTICS_PERIOD_CUSTOM_NAME = "statistics_period_custom"
    }

    @Parcelize
    object DAY : StatisticsPeriod(
        STATISTICS_PERIOD_DAY_NAME,
        currentDate().getStartOfDay(),
        currentDate().getEndOfDay()
    )

    @Parcelize
    object WEEK : StatisticsPeriod(
        STATISTICS_PERIOD_WEEK_NAME,
        currentDate().minus(1, DateTimeUnit.WEEK).getStartOfDay(),
        currentDate().getEndOfDay()
    )

    @Parcelize
    object MONTH : StatisticsPeriod(
        STATISTICS_PERIOD_MONTH_NAME,
        currentDate().minus(1, DateTimeUnit.MONTH).getStartOfDay(),
        currentDate().getEndOfDay()
    )

    @Parcelize
    object YEAR : StatisticsPeriod(
        STATISTICS_PERIOD_YEAR_NAME,
        currentDate().minus(1, DateTimeUnit.YEAR).getStartOfDay(),
        currentDate().getEndOfDay()
    )

    @Parcelize
    data class CUSTOM(val fromDate: @RawValue LocalDateTime, val toDate: @RawValue LocalDateTime) :
        StatisticsPeriod(
            STATISTICS_PERIOD_CUSTOM_NAME,
            fromDate.getStartOfDay(),
            toDate.getEndOfDay()
        )
}
