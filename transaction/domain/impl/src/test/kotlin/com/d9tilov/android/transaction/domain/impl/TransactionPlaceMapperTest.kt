package com.d9tilov.android.transaction.domain.impl

import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.places.domain.model.Place
import com.d9tilov.android.transaction.domain.impl.mapper.toDataModel
import com.d9tilov.android.transaction.domain.impl.mapper.toDomainModel
import com.d9tilov.android.transaction.domain.model.Transaction
import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionPlaceMapperTest {
    @Test
    fun `place decision survives domain mapping`() {
        listOf(null, "", "My cafe").forEach { name ->
            val original =
                Transaction.EMPTY.copy(
                    place = Place(location = LocationData(51.5, -0.1), name = name, address = "High Street"),
                )
            val restored = original.toDataModel().toDomainModel(original.category)
            assertEquals(original.place, restored.place)
        }
    }
}
