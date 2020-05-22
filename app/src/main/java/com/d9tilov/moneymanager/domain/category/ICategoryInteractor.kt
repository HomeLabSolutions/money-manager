package com.d9tilov.moneymanager.domain.category

import io.reactivex.Completable

interface ICategoryInteractor {

    fun createDefaultCategories(): Completable
}
