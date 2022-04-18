package com.d9tilov.moneymanager.statistics.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class StatisticsMenuType(val id: Int) : Parcelable {

    @Parcelize
    object CURRENCY : StatisticsMenuType(0)

    @Parcelize
    object CHART : StatisticsMenuType(1)

    @Parcelize
    object CATEGORY_TYPE : StatisticsMenuType(2)

    @Parcelize
    object TRANSACTION_TYPE : StatisticsMenuType(3)

    @Parcelize
    object STATISTICS : StatisticsMenuType(4)
}
