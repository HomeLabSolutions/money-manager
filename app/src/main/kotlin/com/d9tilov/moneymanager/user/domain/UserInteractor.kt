package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.base.domain.Interactor
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserInteractor : Interactor {

    fun getCurrentUser(): Flow<UserProfile?>
    suspend fun getCurrentCurrency(): String
    suspend fun getFiscalDay(): Int
    suspend fun showPrepopulate(): Boolean
    suspend fun prepopulateCompleted()
    suspend fun createUser(user: FirebaseUser?): UserProfile
    suspend fun updateUser(userProfile: UserProfile)
    suspend fun updateCurrency(code: String)
    suspend fun deleteUser()
}
