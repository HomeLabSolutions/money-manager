package com.d9tilov.moneymanager.category.data

import com.d9tilov.moneymanager.category.ICategoryRepo
import com.d9tilov.moneymanager.category.data.local.ICategoryLocalSource

class CategoryRepo(private val categoryLocalSource: ICategoryLocalSource) :
    ICategoryRepo {

    override fun createDefaultCategories() = categoryLocalSource.createDefaultCategories()
}
