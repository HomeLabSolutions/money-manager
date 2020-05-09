package com.d9tilov.moneymanager.domain.category

import io.reactivex.Completable

interface CategoryRepo {

    fun createDefaultCategories(): Completable
}
