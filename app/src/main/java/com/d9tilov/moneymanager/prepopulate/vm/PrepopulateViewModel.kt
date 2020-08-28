package com.d9tilov.moneymanager.prepopulate.vm

import androidx.hilt.lifecycle.ViewModelInject
import com.d9tilov.moneymanager.base.ui.navigator.PrepopulateNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.prepopulate.domain.PrepopulateInteractor

class PrepopulateViewModel @ViewModelInject constructor(private val prepopulateInteractor: PrepopulateInteractor) :
    BaseViewModel<PrepopulateNavigator>()
