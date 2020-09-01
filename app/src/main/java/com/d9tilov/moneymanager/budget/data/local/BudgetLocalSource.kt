package com.d9tilov.moneymanager.budget.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.data.local.mapper.BudgetMapper
import io.reactivex.Completable
import io.reactivex.Single

class BudgetLocalSource(
    private val preferencesStore: PreferencesStore,
    appDatabase: AppDatabase,
    private val budgetMapper: BudgetMapper
) : BudgetSource {

    private val budgetDao = appDatabase.budgetDao()

    override fun insert(budgetData: BudgetData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            budgetDao.insert(
                budgetMapper.toDbModel(
                    budgetData.copy(
                        clientId = currentUserId,
                        currencyCode = preferencesStore.baseCurrencyCode
                    )
                )
            )
        }
    }

    override fun get(): Single<BudgetData> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Single.error(WrongUidException())
        } else {
            budgetDao.get(currentUserId).map { budgetMapper.toDataModel(it) }
        }
    }

    override fun getCount(): Single<Int> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Single.error(WrongUidException())
        } else {
            budgetDao.getCount(currentUserId)
        }
    }

    override fun update(budgetData: BudgetData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            budgetDao.update(budgetMapper.toDbModel(budgetData))
        }
    }

    override fun delete(budgetData: BudgetData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            budgetDao.delete(budgetMapper.toDbModel(budgetData))
        }
    }
}
