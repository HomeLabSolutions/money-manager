package com.d9tilov.moneymanager.data.category.local

import com.d9tilov.moneymanager.data.base.Source
import com.d9tilov.moneymanager.data.category.entities.Category
import io.reactivex.Completable
import io.reactivex.Flowable

interface CategoryLocalSource : Source {

    fun createDefaultCategories(): Completable
    fun create(category: Category): Completable
    fun getById(id: Long): Flowable<Category>
    fun getByParentId(id: Long): Flowable<List<Category>>
    fun getAll(): Flowable<List<Category>>
    fun update(category: Category): Completable
    fun delete(category: Category): Completable
}
