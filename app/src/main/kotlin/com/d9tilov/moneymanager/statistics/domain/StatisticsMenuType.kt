package com.d9tilov.moneymanager.statistics.domain

enum class StatisticsMenuType {
    CURRENCY,
    CHART,
    CATEGORY_TYPE,
    TRANSACTION_TYPE,
    STATISTICS
}

fun toType(number: Int) = StatisticsMenuType.values()[number]
