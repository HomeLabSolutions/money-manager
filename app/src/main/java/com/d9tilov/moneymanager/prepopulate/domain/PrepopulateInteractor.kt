package com.d9tilov.moneymanager.prepopulate.domain

import io.reactivex.Single

interface PrepopulateInteractor {

    fun showPrepopulate(): Single<Boolean>
}
