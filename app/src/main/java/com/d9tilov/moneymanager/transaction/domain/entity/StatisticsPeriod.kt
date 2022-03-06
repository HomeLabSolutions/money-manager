package com.d9tilov.moneymanager.transaction.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class StatisticsPeriod(val name: String) : Parcelable {

    companion object {
        private const val STATISTICS_PERIOD_DAY_NAME = "statistics_period_day"
        private const val STATISTICS_PERIOD_WEEK_NAME = "statistics_period_week"
        private const val STATISTICS_PERIOD_MONTH_NAME = "statistics_period_month"
        private const val STATISTICS_PERIOD_YEAR_NAME = "statistics_period_year"
        private const val STATISTICS_PERIOD_CUSTOM_NAME = "statistics_period_custom"
    }

    @Parcelize
    object DAY : StatisticsPeriod(STATISTICS_PERIOD_DAY_NAME)

    @Parcelize
    object WEEK : StatisticsPeriod(STATISTICS_PERIOD_WEEK_NAME)

    @Parcelize
    object MONTH : StatisticsPeriod(STATISTICS_PERIOD_MONTH_NAME)

    @Parcelize
    object YEAR : StatisticsPeriod(STATISTICS_PERIOD_YEAR_NAME)

    @Parcelize
    object CUSTOM : StatisticsPeriod(STATISTICS_PERIOD_CUSTOM_NAME)
}
