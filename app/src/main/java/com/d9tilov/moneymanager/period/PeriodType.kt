package com.d9tilov.moneymanager.period

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class PeriodType(open val name: String) : Parcelable {

    companion object {
        const val YEAR_NAME = "every_year"
        const val MONTH_NAME = "every_month"
        const val WEEK_NAME = "every_week"
        const val DAY_NAME = "every_day"
    }

    @Parcelize
    object YEAR : PeriodType(YEAR_NAME)

    @Parcelize
    object MONTH : PeriodType(MONTH_NAME)

    @Parcelize
    object WEEK : PeriodType(WEEK_NAME)

    @Parcelize
    object DAY : PeriodType(DAY_NAME)
}
