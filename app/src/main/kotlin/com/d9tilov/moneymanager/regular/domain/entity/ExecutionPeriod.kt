package com.d9tilov.moneymanager.regular.domain.entity

import android.os.Parcelable
import kotlinx.datetime.LocalDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed class ExecutionPeriod(val periodType: PeriodType, val lastExecutionDateTime: LocalDateTime) :
    Parcelable {

    @Parcelize
    data class EveryMonth(val dayOfMonth: Int, val lastExecDate: @RawValue LocalDateTime) :
        ExecutionPeriod(PeriodType.MONTH, lastExecDate)

    @Parcelize
    data class EveryWeek(val dayOfWeek: Int, val lastExecDate: @RawValue LocalDateTime) :
        ExecutionPeriod(PeriodType.WEEK, lastExecDate)

    @Parcelize
    data class EveryDay(val nextExecDate: @RawValue LocalDateTime) :
        ExecutionPeriod(PeriodType.DAY, nextExecDate)
}
