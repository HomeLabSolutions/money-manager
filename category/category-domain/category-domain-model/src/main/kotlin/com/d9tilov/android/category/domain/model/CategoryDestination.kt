package com.d9tilov.android.category.domain.model

enum class CategoryDestination {
    MAIN_SCREEN,
    MAIN_WITH_SUM_SCREEN,
    EDIT_TRANSACTION_SCREEN,
    EDIT_REGULAR_TRANSACTION_SCREEN,
    CATEGORY_CREATION_SCREEN,
    CATEGORY_SCREEN,
    SUB_CATEGORY_SCREEN,
}

fun Int.toDestination() = CategoryDestination.entries.firstOrNull { it.ordinal == this }
