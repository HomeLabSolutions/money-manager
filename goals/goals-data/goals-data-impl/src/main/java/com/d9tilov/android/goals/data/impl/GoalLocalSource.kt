package com.d9tilov.android.goals.data.impl

import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.database.dao.GoalDao
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.goals.data.contract.GoalSource
import com.d9tilov.android.goals.data.impl.mapper.toDataModel
import com.d9tilov.android.goals.data.impl.mapper.toDbModel
import com.d9tilov.android.goals.domain.model.GoalData
import com.d9tilov.android.network.dispatchers.Dispatcher
import com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GoalLocalSource @Inject constructor(
    @Dispatcher(MoneyManagerDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    private val preferencesStore: PreferencesStore,
    private val goalDao: GoalDao
) : GoalSource {

    override suspend fun insert(goalData: GoalData) = withContext(dispatcher) {
        val currentUserId = preferencesStore.uid.firstOrNull()
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            goalDao.insert(goalData.copy(clientId = currentUserId).toDbModel())
        }
    }

    override fun getAll(): Flow<List<GoalData>> =
        preferencesStore.uid
            .filterNotNull()
            .flatMapMerge { uid ->
                goalDao.getAll(uid).map { list -> list.map { it.toDataModel() } }
            }
            .flowOn(dispatcher)

    override suspend fun update(goalData: GoalData) = withContext(dispatcher) {
        val currentUserId = preferencesStore.uid.firstOrNull()
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            goalDao.update(goalData.toDbModel())
        }
    }

    override suspend fun delete(goalData: GoalData) = withContext(dispatcher) {
        val currentUserId = preferencesStore.uid.firstOrNull()
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            goalDao.delete(goalData.toDbModel())
        }
    }
}
