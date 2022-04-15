package com.d9tilov.moneymanager.user.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserSource : Source {

    suspend fun createUserOrRestore(userProfile: UserProfile): UserProfile
    suspend fun updateCurrentUser(userProfile: UserProfile)
    suspend fun getFiscalDay(): Int
    fun getCurrentUser(): Flow<UserProfile>
    suspend fun deleteUser()
}
