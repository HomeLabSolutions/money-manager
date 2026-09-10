package com.d9tilov.android.transaction.data.impl

import com.d9tilov.android.database.dao.TransactionDao
import com.d9tilov.android.database.entity.TransactionMinMaxDateDbModel
import com.d9tilov.android.datastore.PreferencesStore
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionLocalSourceTest {
    private val preferencesStore: PreferencesStore = mockk()
    private val transactionDao: TransactionDao = mockk()
    private val source =
        TransactionLocalSource(
            ioDispatcher = StandardTestDispatcher(),
            preferencesStore = preferencesStore,
            transactionDao = transactionDao,
        )

    @Test
    fun `getMinMaxDate returns dates only for the current user`() =
        runTest {
            val currentUserDates =
                TransactionMinMaxDateDbModel(
                    minDate = LocalDateTime(2025, 1, 1, 0, 0),
                    maxDate = LocalDateTime(2025, 12, 31, 0, 0),
                )
            every { preferencesStore.uid } returns flowOf(CURRENT_USER_ID)
            coEvery { transactionDao.getMinMaxDate(CURRENT_USER_ID) } returns currentUserDates

            val result = source.getMinMaxDate()

            assertEquals(currentUserDates.minDate, result.minDate)
            assertEquals(currentUserDates.maxDate, result.maxDate)
            coVerify(exactly = 1) { transactionDao.getMinMaxDate(CURRENT_USER_ID) }
        }

    private companion object {
        const val CURRENT_USER_ID = "current-user"
    }
}
