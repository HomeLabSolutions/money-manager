package com.d9tilov.moneymanager.budget.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.data.local.mapper.BudgetMapper

class BudgetLocalSource(
    private val preferencesStore: PreferencesStore,
    appDatabase: AppDatabase,
    private val budgetMapper: BudgetMapper
) : BudgetSource {

    private val budgetDao = appDatabase.budgetDao()

    override suspend fun insert(budgetData: BudgetData) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            budgetDao.insert(
                budgetMapper.toDbModel(
                    budgetData.copy(
                        clientId = currentUserId
                    )
                )
            )
        }
    }

    override suspend fun get(): BudgetData {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            val budgetDbModel = budgetDao.get(currentUserId)
            budgetMapper.toDataModel(budgetDbModel)
        }
    }

    override suspend fun getCount(): Int {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            budgetDao.getCount(currentUserId)
        }
    }

    override suspend fun update(budgetData: BudgetData) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            budgetDao.update(budgetMapper.toDbModel(budgetData).copy(clientId = currentUserId))
        }
    }

    override suspend fun delete(budgetData: BudgetData) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            budgetDao.delete(budgetMapper.toDbModel(budgetData))
        }
    }
}
