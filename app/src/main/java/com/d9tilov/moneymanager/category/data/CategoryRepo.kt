package com.d9tilov.moneymanager.category.data

import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.ICategoryRepo
import com.d9tilov.moneymanager.category.data.local.ICategoryLocalSource
import io.reactivex.Flowable

class CategoryRepo(private val categoryLocalSource: ICategoryLocalSource) :
    ICategoryRepo {

    override fun createExpenseDefaultCategories() = categoryLocalSource.createExpenseDefaultCategories()
    override fun getExpenseCategories() = categoryLocalSource.getAllExpense()
}
