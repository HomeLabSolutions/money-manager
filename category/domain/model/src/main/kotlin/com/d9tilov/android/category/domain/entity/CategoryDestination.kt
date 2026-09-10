package com.d9tilov.android.category.domain.entity

enum class CategoryDestination {
    MAIN_SCREEN,
    MAIN_WITH_SUM_SCREEN,
    EDIT_TRANSACTION_SCREEN,
    EDIT_REGULAR_TRANSACTION_SCREEN,
}

fun Int.toDestination() = CategoryDestination.entries.firstOrNull { it.ordinal == this }
