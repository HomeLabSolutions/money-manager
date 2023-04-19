package com.d9tilov.android.regular.transaction.ui.vm

import androidx.lifecycle.SavedStateHandle
import com.d9tilov.android.common_android.ui.base.BaseViewModel
import com.d9tilov.android.regular.transaction.ui.navigator.DayOfMonthDialogNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DayOfMonthViewModel @Inject constructor(savedStateHandle: SavedStateHandle) :
    BaseViewModel<DayOfMonthDialogNavigator>() {

    var dayOfMonth: Int = savedStateHandle.get<Int>("day_of_month") ?: 1
}
