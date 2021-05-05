package com.d9tilov.moneymanager.goal.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.goal.data.entity.GoalData
import com.d9tilov.moneymanager.goal.data.local.mapper.GoalDataMapper
import io.reactivex.Completable
import io.reactivex.Flowable

class GoalLocalSource(
    private val preferencesStore: PreferencesStore,
    appDatabase: AppDatabase,
    private val goalDataMapper: GoalDataMapper
) : GoalSource {

    private val goalDao = appDatabase.goalDao()

    override fun insert(goalData: GoalData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            goalDao.insert(
                goalDataMapper.toDbModel(
                    goalData.copy(
                        clientId = currentUserId
                    )
                )
            )
        }
    }

    override fun getAll(): Flowable<List<GoalData>> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Flowable.error(WrongUidException())
        } else {
            goalDao.getAll(currentUserId)
                .map { list -> list.map { goalDataMapper.toDataModel(it) } }
        }
    }

    override fun update(goalData: GoalData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            goalDao.update(goalDataMapper.toDbModel(goalData))
        }
    }

    override fun delete(goalData: GoalData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            goalDao.delete(goalDataMapper.toDbModel(goalData))
        }
    }
}
