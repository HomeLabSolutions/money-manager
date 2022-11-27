package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.domain.mapper.UserDomainMapper
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

class UserInfoInteractor(
    private val userRepo: UserRepo,
    private val userDomainMapper: UserDomainMapper
) : UserInteractor {

    override fun getCurrentUser(): Flow<UserProfile?> = userRepo.getCurrentUser()

    override suspend fun getFiscalDay(): Int = userRepo.getFiscalDay()
    override suspend fun showPrepopulate(): Boolean = userRepo.showPrepopulate()
    override suspend fun prepopulateCompleted() = userRepo.prepopulateCompleted()

    override suspend fun createUser(user: FirebaseUser?): UserProfile =
        userRepo.create(userDomainMapper.toDataModel(user))

    override suspend fun updateFiscalDay(fiscalDay: Int) = userRepo.updateFiscalDay(fiscalDay)

    override suspend fun deleteUser() = userRepo.delete()
}
