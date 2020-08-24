package com.d9tilov.moneymanager.accumulations.data.local

import com.d9tilov.moneymanager.accumulations.data.entity.Accumulation
import com.d9tilov.moneymanager.accumulations.data.mapper.AccumulationDataMapper
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import io.reactivex.Completable

class AccumulationLocalSource(
    private val preferencesStore: PreferencesStore,
    appDatabase: AppDatabase,
    private val accumulationDataMapper: AccumulationDataMapper
) : AccumulationSource {

    private val accumulationDao = appDatabase.accumulationDao()

    override fun insert(accumulation: Accumulation): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            accumulationDao.insert(accumulationDataMapper.toDbModel(accumulation))
        }
    }

    override fun update(accumulation: Accumulation): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            accumulationDao.update(accumulationDataMapper.toDbModel(accumulation))
        }
    }
}
