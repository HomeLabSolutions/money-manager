package com.d9tilov.moneymanager.regular.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.regular.data.entity.RegularTransactionData
import com.d9tilov.moneymanager.regular.data.local.mapper.RegularTransactionDataMapper
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RegularTransactionLocalSource(
    private val preferencesStore: PreferencesStore,
    appDatabase: AppDatabase,
    private val regularTransactionDataMapper: RegularTransactionDataMapper
) : RegularTransactionSource {

    private val standingDao = appDatabase.regularTransactionDao()

    override suspend fun insert(regularTransactionData: RegularTransactionData) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            standingDao.insert(
                regularTransactionDataMapper.toDbModel(
                    regularTransactionData.copy(
                        clientId = currentUserId,
                    )
                )
            )
        }
    }

    override fun getAll(type: TransactionType): Flow<List<RegularTransactionData>> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            standingDao.getAll(currentUserId, type)
                .map { it.map { item -> regularTransactionDataMapper.toDataModel(item) } }
        }
    }

    override suspend fun update(regularTransactionData: RegularTransactionData) {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            standingDao.update(regularTransactionDataMapper.toDbModel(regularTransactionData))
        }
    }

    override suspend fun delete(regularTransactionData: RegularTransactionData) {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            standingDao.delete(regularTransactionDataMapper.toDbModel(regularTransactionData))
        }
    }
}
