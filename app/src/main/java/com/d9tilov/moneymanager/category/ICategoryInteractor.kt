package com.d9tilov.moneymanager.category

import io.reactivex.Completable

interface ICategoryInteractor {

    fun createDefaultCategories(): Completable
}
