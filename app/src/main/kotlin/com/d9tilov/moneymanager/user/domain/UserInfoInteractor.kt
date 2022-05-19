package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_CODE
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.domain.mapper.UserDomainMapper
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class UserInfoInteractor(
    private val userRepo: UserRepo,
    private val userDomainMapper: UserDomainMapper
) : UserInteractor {

    override fun getCurrentUser(): Flow<UserProfile?> = userRepo.getCurrentUser()
    override suspend fun getCurrentCurrency(): String = getCurrentUser().firstOrNull()?.currentCurrencyCode ?: DEFAULT_CURRENCY_CODE

    override suspend fun getFiscalDay(): Int = userRepo.getFiscalDay()
    override suspend fun showPrepopulate(): Boolean = userRepo.showPrepopulate()
    override suspend fun prepopulateCompleted() = userRepo.prepopulateCompleted()

    override suspend fun createUser(user: FirebaseUser?): UserProfile =
        userRepo.create(userDomainMapper.toDataModel(user))

    override suspend fun updateUser(userProfile: UserProfile) = userRepo.update(userProfile)

    override suspend fun updateCurrency(code: String) = userRepo.updateCurrency(code)

    override suspend fun deleteUser() = userRepo.delete()
}
