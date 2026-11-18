package com.d9tilov.android.database.converters

import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.model.PeriodType
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

class ModelTypeConverterTest {
    private val lastExecutionDate = LocalDateTime(2024, 2, 29, 0, 0)

    @Test
    fun `annual schedule preserves February 29 and execution date after storage`() {
        val period = ExecutionPeriod.EveryYear(2, 29, lastExecutionDate)
        val stored = ModelTypeConverter.fromExecutionPeriod(period)

        assertEquals(period, ModelTypeConverter.toExecutionPeriod(stored))
        val storedType = ModelTypeConverter.fromPeriodType(PeriodType.YEAR)
        assertEquals(PeriodType.YEAR, ModelTypeConverter.toPeriodType(storedType))
    }

    @Test
    fun `existing schedule formats remain readable`() {
        val timestamp = DateConverter.fromOffsetDateTime(lastExecutionDate)

        assertEquals(
            ExecutionPeriod.EveryDay(lastExecutionDate),
            ModelTypeConverter.toExecutionPeriod("every_day:1:$timestamp"),
        )
        assertEquals(
            ExecutionPeriod.EveryWeek(3, lastExecutionDate),
            ModelTypeConverter.toExecutionPeriod("every_week:3:$timestamp"),
        )
        assertEquals(
            ExecutionPeriod.EveryMonth(29, lastExecutionDate),
            ModelTypeConverter.toExecutionPeriod("every_month:29:$timestamp"),
        )
    }
}
