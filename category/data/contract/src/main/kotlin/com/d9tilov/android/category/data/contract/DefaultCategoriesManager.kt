package com.d9tilov.android.category.data.contract

import com.d9tilov.android.category.domain.entity.DefaultCategory

interface DefaultCategoriesManager {
    fun createDefaultExpenseCategories(): List<DefaultCategory>

    fun createDefaultIncomeCategories(): List<DefaultCategory>
}
