package com.d9tilov.moneymanager.statistics.vm

import com.d9tilov.moneymanager.base.ui.navigator.StatisticsNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor() : BaseViewModel<StatisticsNavigator>()
