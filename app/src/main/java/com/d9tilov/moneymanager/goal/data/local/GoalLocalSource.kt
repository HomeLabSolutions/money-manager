package com.d9tilov.moneymanager.goal.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.goal.data.entity.GoalData
import com.d9tilov.moneymanager.goal.data.local.mapper.GoalDataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GoalLocalSource(
    private val preferencesStore: PreferencesStore,
    appDatabase: AppDatabase,
    private val goalDataMapper: GoalDataMapper
) : GoalSource {

    private val goalDao = appDatabase.goalDao()

    override suspend fun insert(goalData: GoalData) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
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

    override fun getAll(): Flow<List<GoalData>> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            goalDao.getAll(currentUserId)
                .map { list ->
                    list.map { goalDataMapper.toDataModel(it) }
                }
        }
    }

    override suspend fun update(goalData: GoalData) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            goalDao.update(goalDataMapper.toDbModel(goalData))
        }
    }

    override suspend fun delete(goalData: GoalData) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            goalDao.delete(goalDataMapper.toDbModel(goalData))
        }
    }
}
