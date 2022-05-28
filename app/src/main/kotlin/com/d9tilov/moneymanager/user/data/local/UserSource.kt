package com.d9tilov.moneymanager.user.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.base.data.local.preferences.CurrencyMetaData
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserSource : Source {

    fun getCurrentUser(): Flow<UserProfile?>
    fun getCurrentCurrency(): Flow<CurrencyMetaData>
    suspend fun createUserOrRestore(userProfile: UserProfile): UserProfile
    suspend fun updateCurrentUser(userProfile: UserProfile)
    suspend fun updateCurrency(code: String)
    suspend fun showPrepopulate(): Boolean
    suspend fun prepopulateCompleted()
    suspend fun getFiscalDay(): Int
    suspend fun deleteUser()
}
