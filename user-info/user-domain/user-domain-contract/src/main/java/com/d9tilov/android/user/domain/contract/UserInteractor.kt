package com.d9tilov.android.user.domain.contract

import com.d9tilov.android.user.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserInteractor {

    fun getCurrentUser(): Flow<UserProfile?>
    suspend fun getFiscalDay(): Int
    suspend fun showPrepopulate(): Boolean
    suspend fun prepopulateCompleted()
    suspend fun createUser(user: UserProfile): UserProfile
    suspend fun updateFiscalDay(fiscalDay: Int)
    suspend fun deleteUser()
}
