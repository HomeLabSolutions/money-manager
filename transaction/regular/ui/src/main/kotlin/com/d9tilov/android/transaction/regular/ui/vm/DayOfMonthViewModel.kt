package com.d9tilov.android.transaction.regular.ui.vm

import androidx.lifecycle.SavedStateHandle
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.transaction.regular.ui.navigator.DayOfMonthDialogNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DayOfMonthViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
    ) : BaseViewModel<DayOfMonthDialogNavigator>() {
        var dayOfMonth: Int = checkNotNull(savedStateHandle["day_of_month"])
    }
