package com.d9tilov.moneymanager.fixed.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.fixed.data.entity.FixedTransactionData
import com.d9tilov.moneymanager.fixed.data.local.mapper.FixedTransactionDataMapper
import com.d9tilov.moneymanager.transaction.TransactionType
import io.reactivex.Completable
import io.reactivex.Flowable

class FixedTransactionLocalSource(
    private val preferencesStore: PreferencesStore,
    appDatabase: AppDatabase,
    private val fixedTransactionDataMapper: FixedTransactionDataMapper
) : FixedTransactionSource {

    private val standingDao = appDatabase.fixedDao()

    override fun insert(fixedTransactionData: FixedTransactionData): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            standingDao.insert(
                fixedTransactionDataMapper.toDbModel(
                    fixedTransactionData.copy(
                        clientId = currentUserId,
                        currencyCode = preferencesStore.baseCurrencyCode
                    )
                )
            )
        }
    }

    override fun getAll(type: TransactionType): Flowable<List<FixedTransactionData>> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Flowable.error(WrongUidException())
        } else {
            standingDao.getAll(currentUserId, type)
                .map { list -> list.map { fixedTransactionDataMapper.toDataModel(it) } }
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
