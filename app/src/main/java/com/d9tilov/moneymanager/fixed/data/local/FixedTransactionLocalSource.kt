package com.d9tilov.moneymanager.fixed.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.fixed.data.entity.FixedTransactionData
import com.d9tilov.moneymanager.fixed.data.local.mapper.FixedTransactionDataMapper
import io.reactivex.Completable

class FixedTransactionLocalSource(
    private val preferencesStore: PreferencesStore,
    appDatabase: AppDatabase,
    private val fixedTransactionDataMapper: FixedTransactionDataMapper
) : FixedTransactionSource {

    private val standingDao = appDatabase.standingDao()

    override fun insert(fixedTransactionData: FixedTransactionData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            standingDao.insert(fixedTransactionDataMapper.toDbModel(fixedTransactionData))
        }
    }

    override fun update(fixedTransactionData: FixedTransactionData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            standingDao.update(fixedTransactionDataMapper.toDbModel(fixedTransactionData))
        }
    }

    override fun delete(fixedTransactionData: FixedTransactionData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            standingDao.delete(fixedTransactionDataMapper.toDbModel(fixedTransactionData))
        }
    }
}
