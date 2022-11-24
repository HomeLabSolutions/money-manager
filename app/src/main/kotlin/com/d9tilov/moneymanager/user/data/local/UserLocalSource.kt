package com.d9tilov.moneymanager.user.data.local

import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.CurrencyMetaData
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.core.util.CurrencyUtils.getSymbolByCode
import com.d9tilov.moneymanager.user.data.entity.UserDbModel
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.data.local.mapper.toDataModel
import com.d9tilov.moneymanager.user.data.local.mapper.toDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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
        } else dbUser.toDataModel()
    }

    override suspend fun updateFiscalDay(fiscalDay: Int) {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.firstOrNull() }
            ?: throw WrongUidException()
        val user = userDao.getById(currentUserId).firstOrNull()
        user?.let { userDao.update(it.copy(fiscalDay = fiscalDay)) }
    }

    override suspend fun updateCurrency(code: String) {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.firstOrNull() }
            ?: throw WrongUidException()
        userDao.getById(currentUserId).firstOrNull()?.also {
            coroutineScope { launch { userDao.update(it.copy(currentCurrencyCode = code)) } }
        }
    }

    override suspend fun showPrepopulate(): Boolean {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.firstOrNull() }
        return if (currentUserId == null) throw WrongUidException()
        else userDao.showPrepopulate(currentUserId)
    }

    override suspend fun prepopulateCompleted() {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.firstOrNull() }
            ?: throw WrongUidException()
        val user = userDao.getById(currentUserId).firstOrNull()
        user?.let { userDao.update(it.copy(showPrepopulate = false)) }
    }

    override suspend fun getFiscalDay(): Int {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.firstOrNull() }
        return if (currentUserId == null) 1
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

    override fun getCurrentCurrency(): Flow<CurrencyMetaData> = preferencesStore.uid
        .filterNotNull()
        .flatMapMerge { uid ->
            userDao.getCurrentCurrency(uid).map { currencyCode: String? ->
                if (currencyCode == null) CurrencyMetaData.EMPTY
                else CurrencyMetaData(currencyCode, currencyCode.getSymbolByCode())
            }
        }

    override suspend fun deleteUser() {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.firstOrNull() }
        if (currentUserId == null) throw WrongUidException()
        else {
            preferencesStore.clearAllData()
            userDao.deleteUser(currentUserId)
        }
    }
}
