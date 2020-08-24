package com.d9tilov.moneymanager.budget.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.data.mapper.BudgeDataMapper
import io.reactivex.Completable

class BudgetLocalSource(
    private val preferencesStore: PreferencesStore,
    appDatabase: AppDatabase,
    private val budgeDataMapper: BudgeDataMapper
) : BudgetSource {

    private val budgetDao = appDatabase.budgetDao()

    override fun insert(budgetData: BudgetData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            budgetDao.insert(budgeDataMapper.toDbModel(budgetData))
        }
    }

    override

    fun update(budgetData: BudgetData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            budgetDao.update(budgeDataMapper.toDbModel(budgetData))
        }
    }

    override fun delete(budgetData: BudgetData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            budgetDao.delete(budgeDataMapper.toDbModel(budgetData))
        }
    }
}
