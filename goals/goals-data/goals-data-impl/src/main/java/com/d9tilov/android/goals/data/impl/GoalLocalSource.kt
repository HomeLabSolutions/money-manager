package com.d9tilov.android.goals.data.impl

import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.database.dao.GoalDao
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.goals.data.contract.GoalSource
import com.d9tilov.android.goals.data.impl.mapper.toDataModel
import com.d9tilov.android.goals.data.impl.mapper.toDbModel
import com.d9tilov.android.goals.data.model.GoalData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GoalLocalSource(
    private val preferencesStore: PreferencesStore,
    private val goalDao: GoalDao
) : GoalSource {

    override suspend fun insert(goalData: GoalData) {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else goalDao.insert(goalData.copy(clientId = currentUserId).toDbModel())
    }

    override fun getAll(): Flow<List<GoalData>> =
        preferencesStore.uid
            .filterNotNull()
            .flatMapMerge { uid ->
                goalDao.getAll(uid).map { list -> list.map { it.toDataModel() } }
            }

    override suspend fun update(goalData: GoalData) {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else goalDao.update(goalData.toDbModel())
    }

    override suspend fun delete(goalData: GoalData) {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else goalDao.delete(goalData.toDbModel())
    }
}
