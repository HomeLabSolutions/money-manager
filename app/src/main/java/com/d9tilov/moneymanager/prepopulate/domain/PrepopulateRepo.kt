package com.d9tilov.moneymanager.prepopulate.domain

import io.reactivex.Single

interface PrepopulateRepo {

    fun showPrepopulate(): Single<Boolean>
}
