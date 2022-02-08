package com.d9tilov.moneymanager.period

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class PeriodType(open val name: String) : Parcelable {

    companion object {
        const val MONTH_NAME = "every_month"
        const val WEEK_NAME = "every_week"
        const val DAY_NAME = "every_day"
    }

    @Parcelize
    object MONTH : PeriodType(MONTH_NAME)

    @Parcelize
    object WEEK : PeriodType(WEEK_NAME)

    @Parcelize
    object DAY : PeriodType(DAY_NAME)
}
