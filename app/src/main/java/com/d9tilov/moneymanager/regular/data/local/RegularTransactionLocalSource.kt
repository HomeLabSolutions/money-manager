package com.d9tilov.moneymanager.regular.data.local

import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.regular.data.entity.RegularTransactionData
import com.d9tilov.moneymanager.regular.data.local.mapper.toDataModel
import com.d9tilov.moneymanager.regular.data.local.mapper.toDbModel
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RegularTransactionLocalSource(
    private val preferencesStore: PreferencesStore,
    private val standingDao: RegularTransactionDao
) : RegularTransactionSource {

    override suspend fun insert(regularTransactionData: RegularTransactionData) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            standingDao.insert(regularTransactionData.copy(clientId = currentUserId).toDbModel())
        }
    }

    override fun getAll(type: TransactionType): Flow<List<RegularTransactionData>> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            standingDao.getAll(currentUserId, type)
                .map { it.map { item -> item.toDataModel() } }
        }
    }

    override suspend fun getById(id: Long): RegularTransactionData {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            standingDao.getById(currentUserId, id).toDataModel()
        }
    }

    override suspend fun update(regularTransactionData: RegularTransactionData) {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            standingDao.update(regularTransactionData.toDbModel())
        }
    }

    override suspend fun delete(regularTransactionData: RegularTransactionData) {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            standingDao.delete(regularTransactionData.toDbModel())
        }
    }
}
