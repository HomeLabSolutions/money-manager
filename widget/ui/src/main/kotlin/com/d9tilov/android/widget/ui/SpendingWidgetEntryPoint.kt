package com.d9tilov.android.widget.ui

import com.d9tilov.android.widget.domain.contract.SpendingWidgetInteractor

interface SpendingWidgetEntryPoint {
    fun spendingWidgetInteractor(): SpendingWidgetInteractor
}
