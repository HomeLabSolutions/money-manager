package com.d9tilov.moneymanager.periodic.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.periodic.data.entity.PeriodicTransactionData
import com.d9tilov.moneymanager.periodic.data.local.mapper.PeriodicTransactionDataMapper
import com.d9tilov.moneymanager.transaction.TransactionType
import io.reactivex.Completable
import io.reactivex.Flowable

class PeriodicTransactionLocalSource(
    private val preferencesStore: PreferencesStore,
    appDatabase: AppDatabase,
    private val periodicTransactionDataMapper: PeriodicTransactionDataMapper
) : PeriodicTransactionSource {

    private val standingDao = appDatabase.periodicTransactionDao()

    override fun insert(periodicTransactionData: PeriodicTransactionData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            standingDao.insert(
                periodicTransactionDataMapper.toDbModel(
                    periodicTransactionData.copy(
                        clientId = currentUserId,
                    )
                )
            )
        }
    }

    override fun getAll(type: TransactionType): Flowable<List<PeriodicTransactionData>> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Flowable.error(WrongUidException())
        } else {
            standingDao.getAll(currentUserId, type)
                .map { list -> list.map { periodicTransactionDataMapper.toDataModel(it) } }
        }
    }

    override fun update(periodicTransactionData: PeriodicTransactionData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            standingDao.update(periodicTransactionDataMapper.toDbModel(periodicTransactionData))
        }
    }

    override fun delete(periodicTransactionData: PeriodicTransactionData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            standingDao.delete(periodicTransactionDataMapper.toDbModel(periodicTransactionData))
        }
    }
}
