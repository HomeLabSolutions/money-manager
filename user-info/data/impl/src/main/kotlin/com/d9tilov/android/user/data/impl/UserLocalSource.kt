package com.d9tilov.android.user.data.impl

import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.database.dao.UserDao
import com.d9tilov.android.database.entity.UserDbModel
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.user.data.contract.UserSource
import com.d9tilov.android.user.data.impl.mapper.toDataModel
import com.d9tilov.android.user.data.impl.mapper.toDbModel
import com.d9tilov.android.user.domain.model.UserProfile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalSource @Inject constructor(
    private val preferencesStore: PreferencesStore,
    private val userDao: UserDao,
) : UserSource {
    override suspend fun createUserOrRestore(userProfile: UserProfile): UserProfile {
        val currentUserId =
            preferencesStore.uid.firstOrNull()
                ?: throw WrongUidException()
        val dbUser = userDao.getById(currentUserId).firstOrNull()
        return if (dbUser == null) {
            userDao.insert(userProfile.toDbModel().copy(uid = currentUserId))
            userProfile
        } else {
            dbUser.toDataModel()
        }
    }

    override suspend fun updateFiscalDay(fiscalDay: Int) {
        val currentUserId =
            preferencesStore.uid.firstOrNull()
                ?: throw WrongUidException()
        val user = userDao.getById(currentUserId).firstOrNull()
        user?.let { userDao.update(it.copy(fiscalDay = fiscalDay)) }
    }

    override suspend fun showPrepopulate(): Boolean {
        val currentUserId = preferencesStore.uid.firstOrNull()
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            userDao.showPrepopulate(currentUserId)
        }
    }

    override suspend fun prepopulateCompleted() {
        val currentUserId =
            preferencesStore.uid.firstOrNull()
                ?: throw WrongUidException()
        val user = userDao.getById(currentUserId).firstOrNull()
        user?.let { userDao.update(it.copy(showPrepopulate = false)) }
    }

    override suspend fun getFiscalDay(): Int {
        val currentUserId = preferencesStore.uid.firstOrNull()
        return if (currentUserId == null) {
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
            .flatMapMerge { uid -> userDao.getById(uid).map { user: UserDbModel? -> user?.toDataModel() } }

    override suspend fun deleteUser() {
        val currentUserId = preferencesStore.uid.firstOrNull()
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            preferencesStore.clearAllData()
            userDao.deleteUser(currentUserId)
        }
    }
}
