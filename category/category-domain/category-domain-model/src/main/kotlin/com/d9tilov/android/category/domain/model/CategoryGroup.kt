package com.d9tilov.android.category.domain.model

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
