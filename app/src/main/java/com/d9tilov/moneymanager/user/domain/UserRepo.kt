package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.user.data.entity.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepo {
    fun getCurrentUser(): Flow<UserProfile>
    suspend fun getFiscalDay(): Int
    suspend fun create(entity: UserProfile): UserProfile
    suspend fun update(entity: UserProfile)
    suspend fun delete()
}
