package com.d9tilov.moneymanager.prepopulate.data

import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.prepopulate.domain.PrepopulateRepo
import io.reactivex.Single

class PrepopulateDataRepo(
    private val preferencesStore: PreferencesStore
) : PrepopulateRepo {

    override fun showPrepopulate(): Single<Boolean> =
        Single.fromCallable { preferencesStore.showPrepopulate }
}
