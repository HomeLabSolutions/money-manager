package com.d9tilov.moneymanager.user.data.local

import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.data.local.entity.UserDbModel
import com.d9tilov.moneymanager.user.data.local.mapper.toDataModel
import com.d9tilov.moneymanager.user.data.local.mapper.toDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserLocalSource(
    private val preferencesStore: PreferencesStore,
    private val userDao: UserDao
) : UserSource {

    override suspend fun createUserOrRestore(userProfile: UserProfile): UserProfile {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.firstOrNull() }
            ?: throw WrongUidException()
        val dbUser = userDao.getById(currentUserId).firstOrNull()
        return if (dbUser == null) {
            userDao.insert(userProfile.toDbModel().copy(uid = currentUserId))
            userProfile
        } else {
            val currencyCode = dbUser.currentCurrencyCode
            preferencesStore.updateCurrentCurrency(currencyCode)
            dbUser.toDataModel()
        }
    }

    override suspend fun updateCurrentUser(userProfile: UserProfile) {
        userDao.update(userProfile.toDbModel())
    }

    override suspend fun updateCurrency(code: String): Unit = coroutineScope {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
            ?: throw WrongUidException()
        val user = userDao.getById(currentUserId).firstOrNull()
        user?.let {
            awaitAll(
                async { preferencesStore.updateCurrentCurrency(code) },
                async { userDao.update(it.copy(currentCurrencyCode = code)) }
            )
        }
    }

    override suspend fun showPrepopulate(): Boolean {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        return if (currentUserId == null) throw WrongUidException()
        else userDao.showPrepopulate(currentUserId)
    }

    override suspend fun prepopulateCompleted() {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
            ?: throw WrongUidException()
        val user = userDao.getById(currentUserId).firstOrNull()
        user?.let { userDao.update(it.copy(showPrepopulate = false)) }
    }

    override suspend fun getFiscalDay(): Int {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        return if (currentUserId == null) throw WrongUidException()
        else {
            val fiscalDay = userDao.getFiscalDay(currentUserId)
            if (fiscalDay == 0) 1 else fiscalDay
        }
    }

    override fun getCurrentUser(): Flow<UserProfile?> = preferencesStore.uid
        .filterNotNull()
        .flatMapMerge { uid ->
            userDao.getById(uid).map { user: UserDbModel? -> user?.toDataModel() }
        }

    override suspend fun deleteUser() {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else {
            preferencesStore.clearAllData()
            userDao.deleteUser(currentUserId)
        }
    }
}
