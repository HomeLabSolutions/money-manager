package com.d9tilov.android.transaction.regular.data.impl

import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.database.dao.RegularTransactionDao
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.network.dispatchers.Dispatcher
import com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers
import com.d9tilov.android.transaction.regular.data.contract.RegularTransactionSource
import com.d9tilov.android.transaction.regular.data.impl.mapper.toDataModel
import com.d9tilov.android.transaction.regular.data.impl.mapper.toDbModel
import com.d9tilov.android.transaction.regular.domain.model.RegularTransactionData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RegularTransactionLocalSource(
    @Dispatcher(MoneyManagerDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    private val preferencesStore: PreferencesStore,
    private val regularTransactionDao: RegularTransactionDao,
) : RegularTransactionSource {
    override suspend fun insert(regularTransactionData: RegularTransactionData) =
        withContext(dispatcher) {
            val currentUserId = preferencesStore.uid.firstOrNull()
            if (currentUserId == null) {
                throw WrongUidException()
            } else {
                regularTransactionDao.insert(
                    regularTransactionData.copy(clientId = currentUserId).toDbModel(),
                )
            }
        }

    override fun getAll(type: TransactionType): Flow<List<RegularTransactionData>> =
        preferencesStore.uid
            .filterNotNull()
            .flatMapMerge { uid ->
                regularTransactionDao
                    .getAll(uid, type)
                    .map { it.map { item -> item.toDataModel() } }
            }.flowOn(dispatcher)

    override suspend fun getAllByCategory(category: Category): List<RegularTransactionData> =
        withContext(dispatcher) {
            val currentUserId = preferencesStore.uid.firstOrNull()
            if (currentUserId == null) {
                throw WrongUidException()
            } else {
                regularTransactionDao
                    .getByCategoryId(currentUserId, category.id)
                    .map { item -> item.toDataModel() }
            }
        }

    override suspend fun getById(id: Long): RegularTransactionData =
        withContext(dispatcher) {
            val currentUserId = preferencesStore.uid.firstOrNull()
            if (currentUserId == null) {
                throw WrongUidException()
            } else {
                regularTransactionDao.getById(currentUserId, id).toDataModel()
            }
        }

    override suspend fun update(regularTransactionData: RegularTransactionData) =
        withContext(dispatcher) {
            val currentUserId = preferencesStore.uid.firstOrNull()
            if (currentUserId == null) {
                throw WrongUidException()
            } else {
                regularTransactionDao.update(regularTransactionData.toDbModel())
            }
        }

    override suspend fun delete(regularTransactionData: RegularTransactionData) =
        withContext(dispatcher) {
            val currentUserId = preferencesStore.uid.firstOrNull()
            if (currentUserId == null) {
                throw WrongUidException()
            } else {
                regularTransactionDao.delete(regularTransactionData.toDbModel())
            }
        }
}
