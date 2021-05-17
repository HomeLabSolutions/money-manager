package com.d9tilov.moneymanager.incomeexpense.ui.vm

import com.d9tilov.moneymanager.base.ui.navigator.IncomeExpenseNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IncomeExpenseViewModel @Inject constructor() : BaseViewModel<IncomeExpenseNavigator>()
