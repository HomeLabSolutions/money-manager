package com.d9tilov.moneymanager.category.domain.entity

enum class CategoryGroup {
    HOUSING,
    TRANSPORT,
    FOOD,
    UTILITIES,
    INSURANCE,
    MEDICAL,
    SPORT,
    INVESTING,
    RECREATION,
    PERSONAL,
    OTHERS,
    UNKNOWN
}
fun Int.toGroupId() = CategoryGroup.values()[this]
