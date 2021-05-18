package com.d9tilov.moneymanager.budget.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.data.local.mapper.BudgetMapper
import com.d9tilov.moneymanager.core.util.getFirstDayOfMonth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.util.Date

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

    override fun get(): Flow<BudgetData> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            budgetDao.get(currentUserId)
                .map {
                    if (it == null) {
                        val data = BudgetData(
                            clientId = currentUserId,
                            currencyCode = preferencesStore.baseCurrencyCode,
                            sum = BigDecimal.ZERO,
                            createdDate = Date(),
                            fiscalDay = getFirstDayOfMonth()
                        )
                        insert(data)
                        data
                    } else {
                        budgetMapper.toDataModel(it)
                    }
                }
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
