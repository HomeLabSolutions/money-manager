package com.d9tilov.android.user.domain.contract

import com.d9tilov.android.user.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepo {
    fun getCurrentUser(): Flow<UserProfile?>
    suspend fun getFiscalDay(): Int
    suspend fun showPrepopulate(): Boolean
    suspend fun prepopulateCompleted()
    suspend fun create(entity: UserProfile): UserProfile
    suspend fun updateFiscalDay(fiscalDay: Int)
    suspend fun delete()
}
