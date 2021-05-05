package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.domain.mapper.UserDomainMapper
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class UserInfoInteractor(
    private val userRepo: UserRepo,
    private val userDomainMapper: UserDomainMapper,
) : UserInteractor {

    override fun getCurrentUser(): Flowable<UserProfile> = userRepo.getUser()
    override fun getCurrency(): Single<String> = userRepo.getCurrency()
    override fun getBackupData(): Flowable<BackupData> = userRepo.getBackupData()

    override fun showPrepopulate(): Single<Boolean> = userRepo.showPrepopulate()

    override fun createUser(user: FirebaseUser?): Single<UserProfile> {
        val newUser = userDomainMapper.toDataModel(user)
        return userRepo.createUser(newUser).toSingleDefault(newUser)
    }

    override fun updateUser(userProfile: UserProfile): Completable =
        userRepo.updateUser(userProfile)

    override fun backup(): Completable = userRepo.backup()

    override fun logout() = userRepo.logout()
}
