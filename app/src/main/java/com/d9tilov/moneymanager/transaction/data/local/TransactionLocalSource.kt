package com.d9tilov.moneymanager.transaction.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.incomeexpense.data.base.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.data.mapper.TransactionDataMapper
import io.reactivex.Completable
import io.reactivex.Flowable

class TransactionLocalSource(
    private val preferencesStore: PreferencesStore,
    appDatabase: AppDatabase,
    private val transactionDataMapper: TransactionDataMapper
) : TransactionSource {

    private val transactionDao: TransactionDao = appDatabase.transactionDao()

    override fun insert(transaction: TransactionDataModel): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            transactionDao.insert(transactionDataMapper.toDbModel(transaction, currentUserId))
        }
    }

    override fun getById(id: Long): Flowable<TransactionDataModel> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Flowable.error(WrongUidException())
        } else {
            transactionDao.getById(currentUserId, id).map { transactionDataMapper.toDataModel(it) }
        }
    }

    override fun getByType(transactionType: TransactionType): Flowable<List<TransactionDataModel>> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Flowable.error(WrongUidException())
        } else {
            transactionDao.getAllByType(currentUserId, transactionType)
                .map { list ->
                    list.map { transactionDataMapper.toDataModel(it) }
                }
        }
    }

    override fun remove(transaction: TransactionDataModel) = transactionDao.delete(transaction.id)
}
