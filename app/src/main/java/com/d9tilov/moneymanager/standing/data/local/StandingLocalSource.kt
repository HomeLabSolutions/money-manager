package com.d9tilov.moneymanager.standing.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.standing.data.entity.StandingData
import com.d9tilov.moneymanager.standing.data.local.mapper.StandingDataMapper
import io.reactivex.Completable

class StandingLocalSource(
    private val preferencesStore: PreferencesStore,
    appDatabase: AppDatabase,
    private val standingDataMapper: StandingDataMapper
) : StandingSource {

    private val standingDao = appDatabase.standingDao()

    override fun insert(standingData: StandingData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            standingDao.insert(standingDataMapper.toDbModel(standingData))
        }
    }

    override fun update(standingData: StandingData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            standingDao.update(standingDataMapper.toDbModel(standingData))
        }
    }

    override fun delete(standingData: StandingData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            standingDao.delete(standingDataMapper.toDbModel(standingData))
        }
    }
}
