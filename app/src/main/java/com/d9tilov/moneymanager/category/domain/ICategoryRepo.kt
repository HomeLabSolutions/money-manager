package com.d9tilov.moneymanager.category.domain

import com.d9tilov.moneymanager.category.data.entities.Category
import io.reactivex.Completable
import io.reactivex.Flowable

interface ICategoryRepo {

    fun createExpenseDefaultCategories(): Completable
    fun getExpenseCategories(): Flowable<List<Category>>
}
