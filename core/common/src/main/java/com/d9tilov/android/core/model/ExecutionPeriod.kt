package com.d9tilov.android.core.model

import kotlinx.datetime.LocalDateTime

sealed class ExecutionPeriod(val periodType: PeriodType, val lastExecutionDateTime: LocalDateTime) {

    data class EveryMonth(val dayOfMonth: Int, val lastExecDate: LocalDateTime) :
        ExecutionPeriod(PeriodType.MONTH, lastExecDate)

    data class EveryWeek(val dayOfWeek: Int, val lastExecDate: LocalDateTime) :
        ExecutionPeriod(PeriodType.WEEK, lastExecDate)

    data class EveryDay(val nextExecDate: LocalDateTime) :
        ExecutionPeriod(PeriodType.DAY, nextExecDate)
}
