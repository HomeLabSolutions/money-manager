package com.d9tilov.moneymanager.regular.vm

import androidx.lifecycle.SavedStateHandle
import com.d9tilov.moneymanager.base.ui.navigator.DayOfMonthDialogNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class DayOfMonthViewModel @AssistedInject constructor(@Assisted val savedStateHandle: SavedStateHandle) :
    BaseViewModel<DayOfMonthDialogNavigator>() {

    var dayOfMonth: Int = savedStateHandle.get<Int>("day_of_month") ?: 1
}
