package com.d9tilov.android.transaction.data.impl

import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.places.domain.model.LocationData
import com.d9tilov.android.places.domain.model.Place
import com.d9tilov.android.transaction.data.impl.mapper.toDataModel
import com.d9tilov.android.transaction.data.impl.mapper.toDbModel
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class TransactionPlaceDataMapperTest {
    @Test
    fun `custom names skipped names and undecided places survive database mapping`() {
        val transaction =
            TransactionDataModel(
                type = TransactionType.EXPENSE,
                categoryId = 1,
                currencyCode = "USD",
                sum = BigDecimal.ONE,
                usdSum = BigDecimal.ONE,
                isRegular = false,
                inStatistics = true,
                place = Place(location = LocationData(51.5, -0.1)),
            )
        listOf(null, "", "My cafe").forEach { name ->
            val original = transaction.copy(place = transaction.place.copy(name = name, address = "High Street"))
            assertEquals(original, original.toDbModel().toDataModel())
        }
    }
}
