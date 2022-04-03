package com.d9tilov.moneymanager.regular.vm

import androidx.lifecycle.SavedStateHandle
import com.d9tilov.moneymanager.base.ui.navigator.DayOfMonthDialogNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DayOfMonthViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) :
    BaseViewModel<DayOfMonthDialogNavigator>() {

    var dayOfMonth: Int = savedStateHandle.get<Int>("day_of_month") ?: 1
}
