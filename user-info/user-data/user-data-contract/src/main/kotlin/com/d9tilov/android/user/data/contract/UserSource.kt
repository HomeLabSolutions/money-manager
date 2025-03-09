package com.d9tilov.android.user.data.contract

import com.d9tilov.android.user.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserSource {
    fun getCurrentUser(): Flow<UserProfile?>

    suspend fun createUserOrRestore(userProfile: UserProfile): UserProfile

    suspend fun updateFiscalDay(fiscalDay: Int)

    suspend fun showPrepopulate(): Boolean

    suspend fun prepopulateCompleted()

    suspend fun getFiscalDay(): Int

    suspend fun deleteUser()
}
