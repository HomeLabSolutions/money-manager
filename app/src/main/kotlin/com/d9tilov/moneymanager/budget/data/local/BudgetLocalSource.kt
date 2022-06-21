package com.d9tilov.moneymanager.budget.data.local

import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.budget.domain.entity.BudgetData
import com.d9tilov.moneymanager.budget.data.local.mapper.toDataModel
import com.d9tilov.moneymanager.budget.data.local.mapper.toDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class BudgetLocalSource(
    private val preferencesStore: PreferencesStore,
    private val budgetDao: BudgetDao
) : BudgetSource {

    override suspend fun createIfNeeded() {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else {
            val budget = budgetDao.get(currentUserId).firstOrNull()
            if (budget == null) {
                budgetDao.insert(
                    BudgetData.EMPTY.copy(
                        clientId = currentUserId,
                        currencyCode = preferencesStore.currentCurrency.first().code
                    ).toDbModel()
                )
            }
        }
    }

    override fun get(): Flow<BudgetData> =
        preferencesStore.uid
            .filterNotNull()
            .flatMapMerge { uid -> budgetDao.get(uid).filterNotNull().map { it.toDataModel() } }

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
