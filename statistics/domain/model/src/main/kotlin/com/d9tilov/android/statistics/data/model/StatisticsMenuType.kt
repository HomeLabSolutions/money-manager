package com.d9tilov.android.statistics.data.model

enum class StatisticsMenuType {
    CURRENCY,
    CHART,
    TRANSACTION_TYPE,
    STATISTICS,
}

fun toType(number: Int) = StatisticsMenuType.entries[number]
