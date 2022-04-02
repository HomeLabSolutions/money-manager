package com.d9tilov.moneymanager.budget.data.local

import com.d9tilov.moneymanager.base.data.local.exceptions.EmptyDbDataException
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.data.local.mapper.toDataModel
import com.d9tilov.moneymanager.budget.data.local.mapper.toDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class BudgetLocalSource(
    private val preferencesStore: PreferencesStore,
    private val budgetDao: BudgetDao
) : BudgetSource {

    override suspend fun insert(budgetData: BudgetData) {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else budgetDao.insert(budgetData.copy(clientId = currentUserId).toDbModel())
    }

    override fun get(): Flow<BudgetData> =
        preferencesStore.uid.flatMapConcat { uid ->
            if (uid == null) throw WrongUidException()
            else budgetDao.get(uid)
                .map { it?.toDataModel() ?: throw EmptyDbDataException("Budget doesn't exists") }
        }

    override suspend fun update(budgetData: BudgetData) {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else budgetDao.update(budgetData.toDbModel().copy(clientId = currentUserId))
    }

    override suspend fun delete(budgetData: BudgetData) {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else budgetDao.delete(budgetData.toDbModel())
    }
}
