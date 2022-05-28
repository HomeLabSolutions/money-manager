package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.base.data.local.preferences.CurrencyMetaData
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepo {
    fun getCurrentUser(): Flow<UserProfile?>
    fun getCurrentCurrency(): Flow<CurrencyMetaData>
    suspend fun getFiscalDay(): Int
    suspend fun showPrepopulate(): Boolean
    suspend fun prepopulateCompleted()
    suspend fun create(entity: UserProfile): UserProfile
    suspend fun updateCurrency(code: String)
    suspend fun update(entity: UserProfile)
    suspend fun delete()
}
