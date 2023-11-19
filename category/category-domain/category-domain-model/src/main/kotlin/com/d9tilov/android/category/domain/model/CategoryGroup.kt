package com.d9tilov.android.category.domain.model

enum class CategoryGroup(val value: Int) {
    HOUSING(0),
    TRANSPORT(1),
    FOOD(2),
    UTILITIES(3),
    INSURANCE(4),
    MEDICAL(5),
    SPORT(6),
    INVESTING(7),
    RECREATION(8),
    PERSONAL(9),
    OTHERS(10),
    UNKNOWN(-1)
}
fun Int.toGroupId() = CategoryGroup.values()[this]
