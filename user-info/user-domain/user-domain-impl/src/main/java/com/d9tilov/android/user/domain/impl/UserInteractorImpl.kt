package com.d9tilov.android.user.domain.impl

import com.d9tilov.android.user.data.contract.UserRepo
import com.d9tilov.android.user.data.model.UserProfile
import com.d9tilov.android.user.domain.contract.UserInteractor
import kotlinx.coroutines.flow.Flow

class UserInteractorImpl(private val userRepo: UserRepo) : UserInteractor {

    override fun getCurrentUser(): Flow<UserProfile?> = userRepo.getCurrentUser()

    override suspend fun getFiscalDay(): Int = userRepo.getFiscalDay()
    override suspend fun showPrepopulate(): Boolean = userRepo.showPrepopulate()
    override suspend fun prepopulateCompleted() = userRepo.prepopulateCompleted()

    override suspend fun createUser(user: UserProfile): UserProfile = userRepo.create(user)

    override suspend fun updateFiscalDay(fiscalDay: Int) = userRepo.updateFiscalDay(fiscalDay)

    override suspend fun deleteUser() = userRepo.delete()
}
