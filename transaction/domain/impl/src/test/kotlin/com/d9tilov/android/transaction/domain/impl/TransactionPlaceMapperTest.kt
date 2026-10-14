package com.d9tilov.android.transaction.domain.impl

import com.d9tilov.android.transaction.domain.impl.mapper.toDataModel
import com.d9tilov.android.transaction.domain.impl.mapper.toDomainModel
import com.d9tilov.android.transaction.domain.model.Transaction
import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionPlaceMapperTest {
    @Test
    fun `place decision survives domain mapping`() {
        listOf(null, "", "My cafe").forEach { name ->
            val original = Transaction.EMPTY.copy(placeName = name)
            assertEquals(name, original.toDataModel().toDomainModel(original.category).placeName)
        }
    }
}
