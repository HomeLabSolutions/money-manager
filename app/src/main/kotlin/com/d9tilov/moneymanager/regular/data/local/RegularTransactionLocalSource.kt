package com.d9tilov.moneymanager.regular.data.local

import com.d9tilov.android.database.dao.RegularTransactionDao
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.regular.data.entity.RegularTransactionData
import com.d9tilov.moneymanager.regular.data.local.mapper.toDataModel
import com.d9tilov.moneymanager.regular.data.local.mapper.toDbModel
import com.d9tilov.android.core.model.TransactionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RegularTransactionLocalSource(
    private val preferencesStore: PreferencesStore,
    private val regularTransactionDao: RegularTransactionDao
) : RegularTransactionSource {

    override suspend fun insert(regularTransactionData: RegularTransactionData) {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else regularTransactionDao.insert(
            regularTransactionData.copy(clientId = currentUserId).toDbModel()
        )
    }

    override fun getAll(type: TransactionType): Flow<List<RegularTransactionData>> =
        preferencesStore.uid
            .filterNotNull()
            .flatMapMerge { uid ->
                regularTransactionDao.getAll(uid, type)
                    .map { it.map { item -> item.toDataModel() } }
            }

    override suspend fun getAllByCategory(category: Category): List<RegularTransactionData> {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        return if (currentUserId == null) throw WrongUidException()
        else regularTransactionDao.getByCategoryId(currentUserId, category.id)
            .map { item -> item.toDataModel() }
    }

    override suspend fun getById(id: Long): RegularTransactionData {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        return if (currentUserId == null) throw WrongUidException()
        else regularTransactionDao.getById(currentUserId, id).toDataModel()
    }

    override suspend fun update(regularTransactionData: RegularTransactionData) {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        return if (currentUserId == null) throw WrongUidException()
        else regularTransactionDao.update(regularTransactionData.toDbModel())
    }

    override suspend fun delete(regularTransactionData: RegularTransactionData) {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        return if (currentUserId == null) throw WrongUidException()
        else regularTransactionDao.delete(regularTransactionData.toDbModel())
    }
}
