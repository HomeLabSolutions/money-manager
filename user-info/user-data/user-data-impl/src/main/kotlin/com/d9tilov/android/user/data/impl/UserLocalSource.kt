package com.d9tilov.android.user.data.impl

import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.database.dao.UserDao
import com.d9tilov.android.database.entity.UserDbModel
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.network.dispatchers.Dispatcher
import com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers
import com.d9tilov.android.user.data.contract.UserSource
import com.d9tilov.android.user.data.impl.mapper.toDataModel
import com.d9tilov.android.user.data.impl.mapper.toDbModel
import com.d9tilov.android.user.domain.model.UserProfile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserLocalSource(
    @Dispatcher(MoneyManagerDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    private val preferencesStore: PreferencesStore,
    private val userDao: UserDao,
) : UserSource {
    override suspend fun createUserOrRestore(userProfile: UserProfile): UserProfile =
        withContext(dispatcher) {
            val currentUserId =
                preferencesStore.uid.firstOrNull()
                    ?: throw WrongUidException()
            val dbUser = userDao.getById(currentUserId).firstOrNull()
            if (dbUser == null) {
                userDao.insert(userProfile.toDbModel().copy(uid = currentUserId))
                userProfile
            } else {
                dbUser.toDataModel()
            }
        }

    override suspend fun updateFiscalDay(fiscalDay: Int): Unit =
        withContext(dispatcher) {
            val currentUserId =
                preferencesStore.uid.firstOrNull()
                    ?: throw WrongUidException()
            val user = userDao.getById(currentUserId).firstOrNull()
            user?.let { userDao.update(it.copy(fiscalDay = fiscalDay)) }
        }

    override suspend fun showPrepopulate(): Boolean =
        withContext(dispatcher) {
            val currentUserId = preferencesStore.uid.firstOrNull()
            if (currentUserId == null) {
                throw WrongUidException()
            } else {
                userDao.showPrepopulate(currentUserId)
            }
        }

    override suspend fun prepopulateCompleted(): Unit =
        withContext(dispatcher) {
            val currentUserId =
                preferencesStore.uid.firstOrNull()
                    ?: throw WrongUidException()
            val user = userDao.getById(currentUserId).firstOrNull()
            user?.let { userDao.update(it.copy(showPrepopulate = false)) }
        }

    override suspend fun getFiscalDay(): Int =
        withContext(dispatcher) {
            val currentUserId = preferencesStore.uid.firstOrNull()
            if (currentUserId == null) {
                1
            } else {
                val fiscalDay = userDao.getFiscalDay(currentUserId)
                if (fiscalDay == 0) 1 else fiscalDay
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCurrentUser(): Flow<UserProfile?> =
        preferencesStore.uid
            .filterNotNull()
            .flatMapMerge { uid ->
                userDao.getById(uid).map { user: UserDbModel? -> user?.toDataModel() }
            }.flowOn(dispatcher)

    override suspend fun deleteUser() =
        withContext(dispatcher) {
            val currentUserId = preferencesStore.uid.firstOrNull()
            if (currentUserId == null) {
                throw WrongUidException()
            } else {
                preferencesStore.clearAllData()
                userDao.deleteUser(currentUserId)
            }
        }
}
