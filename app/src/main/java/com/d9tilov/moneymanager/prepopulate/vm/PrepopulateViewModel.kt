package com.d9tilov.moneymanager.prepopulate.vm

import androidx.hilt.lifecycle.ViewModelInject
import com.d9tilov.moneymanager.base.ui.navigator.PrepopulateNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.user.domain.UserInteractor

class PrepopulateViewModel @ViewModelInject constructor(private val userInteractor: UserInteractor) :
    BaseViewModel<PrepopulateNavigator>()
