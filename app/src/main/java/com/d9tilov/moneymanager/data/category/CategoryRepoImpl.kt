package com.d9tilov.moneymanager.data.category

import com.d9tilov.moneymanager.data.category.local.CategoryLocalSource
import com.d9tilov.moneymanager.domain.category.CategoryRepo

class CategoryRepoImpl(private val categoryLocalSource: CategoryLocalSource) : CategoryRepo {

    override fun createDefaultCategories() = categoryLocalSource.createDefaultCategories()
}
