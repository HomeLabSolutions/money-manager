package com.d9tilov.moneymanager.statistics.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class StatisticsMenuType : Parcelable {
    CURRENCY,
    CHART,
    CATEGORY_TYPE,
    TRANSACTION_TYPE,
    STATISTICS
}

fun toType(number: Int) = StatisticsMenuType.values()[number]
