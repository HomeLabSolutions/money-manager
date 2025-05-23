package com.d9tilov.android.budget.data.impl

import com.d9tilov.android.budget.data.contract.BudgetSource
import com.d9tilov.android.budget.data.impl.mapper.toDataModel
import com.d9tilov.android.budget.data.impl.mapper.toDbModel
import com.d9tilov.android.budget.domain.model.BudgetData
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.database.dao.BudgetDao
import com.d9tilov.android.datastore.PreferencesStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BudgetLocalSource @Inject constructor(
    private val preferencesStore: PreferencesStore,
    private val budgetDao: BudgetDao,
) : BudgetSource {
    override suspend fun createIfNeeded(currencyCode: String) {
        val currentUserId = preferencesStore.uid.firstOrNull()
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            val budget = budgetDao.get(currentUserId).firstOrNull()
            if (budget == null) {
                budgetDao.insert(
                    BudgetData.EMPTY
                        .copy(
                            clientId = currentUserId,
                            currencyCode = currencyCode,
                        ).toDbModel(),
                )
            }
        }
    }

    override fun get(): Flow<BudgetData> =
        preferencesStore.uid
            .filterNotNull()
            .flatMapMerge { uid -> budgetDao.get(uid).filterNotNull().map { it.toDataModel() } }

    override suspend fun update(budgetData: BudgetData) {
        val currentUserId = preferencesStore.uid.firstOrNull()
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            budgetDao.update(budgetData.toDbModel().copy(clientId = currentUserId))
        }
    }

    override suspend fun delete(budgetData: BudgetData) {
        val currentUserId = preferencesStore.uid.firstOrNull()
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            budgetDao.delete(budgetData.toDbModel())
        }
    }
}
