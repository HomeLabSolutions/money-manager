package com.d9tilov.android.widget.ui

import com.d9tilov.android.widget.domain.contract.SpendingWidgetInteractor
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface SpendingWidgetEntryPoint {
    fun spendingWidgetInteractor(): SpendingWidgetInteractor
}
