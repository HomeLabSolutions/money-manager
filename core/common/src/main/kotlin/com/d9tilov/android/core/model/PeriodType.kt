package com.d9tilov.android.core.model

sealed class PeriodType(
    open val name: String,
) {
    companion object {
        const val MONTH_NAME = "every_month"
        const val WEEK_NAME = "every_week"
        const val DAY_NAME = "every_day"
    }

    object MONTH : PeriodType(MONTH_NAME)

    object WEEK : PeriodType(WEEK_NAME)

    object DAY : PeriodType(DAY_NAME)
}
