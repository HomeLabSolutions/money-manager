package com.d9tilov.android.core.model

import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.getStartOfDay
import kotlinx.datetime.LocalDateTime

sealed class ExecutionPeriod(val periodType: PeriodType, val lastExecutionDateTime: LocalDateTime = currentDate().getStartOfDay()) {

    data class EveryMonth(val dayOfMonth: Int, val lastExecDate: LocalDateTime = currentDate().getStartOfDay()) :
        ExecutionPeriod(PeriodType.MONTH, lastExecDate)

    data class EveryWeek(val dayOfWeek: Int, val lastExecDate: LocalDateTime = currentDate().getStartOfDay()) :
        ExecutionPeriod(PeriodType.WEEK, lastExecDate)

    data class EveryDay(val nextExecDate: LocalDateTime = currentDate().getStartOfDay()) :
        ExecutionPeriod(PeriodType.DAY, nextExecDate)
}
