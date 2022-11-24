package com.d9tilov.moneymanager.user.data

import com.d9tilov.moneymanager.base.data.local.preferences.CurrencyMetaData
import com.d9tilov.moneymanager.core.util.CurrencyUtils.getSymbolByCode
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.data.local.UserCacheSource
import com.d9tilov.moneymanager.user.data.local.UserSource
import com.d9tilov.moneymanager.user.domain.UserRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class UserDataRepo(
    private val userLocalSource: UserSource,
    private val userCacheSource: UserCacheSource
) : UserRepo {

    override fun getCurrentUser(): Flow<UserProfile?> = userLocalSource.getCurrentUser()

    override fun getCurrentCurrency(): Flow<CurrencyMetaData> {
        return userCacheSource.getCurrentCurrency().map { currency ->
            if (currency == null) {
                val currencyFromDb = userLocalSource.getCurrentCurrency().firstOrNull() ?: CurrencyMetaData.EMPTY
                userCacheSource.setCurrentCurrency(currencyFromDb)
                currencyFromDb
            } else currency
        }
    }

    override suspend fun showPrepopulate(): Boolean = userLocalSource.showPrepopulate()
    override suspend fun prepopulateCompleted() = userLocalSource.prepopulateCompleted()

    override suspend fun getFiscalDay(): Int = userLocalSource.getFiscalDay()

    override suspend fun create(entity: UserProfile): UserProfile =
        userLocalSource.createUserOrRestore(entity)

    override suspend fun updateCurrency(code: String) {
        userCacheSource.setCurrentCurrency(CurrencyMetaData(code, code.getSymbolByCode()))
        userLocalSource.updateCurrency(code)
    }

    override suspend fun updateFiscalDay(fiscalDay: Int) =
        userLocalSource.updateFiscalDay(fiscalDay)

    override suspend fun delete() {
        userLocalSource.deleteUser()
    }
}
