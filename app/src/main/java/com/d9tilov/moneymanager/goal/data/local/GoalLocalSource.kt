package com.d9tilov.moneymanager.goal.data.local

import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.goal.data.entity.GoalData
import com.d9tilov.moneymanager.goal.data.local.mapper.toDataModel
import com.d9tilov.moneymanager.goal.data.local.mapper.toDbModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GoalLocalSource(
    private val preferencesStore: PreferencesStore,
    private val goalDao: GoalDao
) : GoalSource {

    override suspend fun insert(goalData: GoalData) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            goalDao.insert(goalData.copy(clientId = currentUserId).toDbModel())
        }
    }

    override fun getAll(): Flow<List<GoalData>> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            goalDao.getAll(currentUserId)
                .map { list -> list.map { it.toDataModel() } }
        }
    }

    override suspend fun update(goalData: GoalData) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            goalDao.update(goalData.toDbModel())
        }
    }

    override suspend fun delete(goalData: GoalData) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            goalDao.delete(goalData.toDbModel())
        }
    }
}
