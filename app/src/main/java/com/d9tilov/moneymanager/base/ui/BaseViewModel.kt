package com.d9tilov.moneymanager.base.ui

import androidx.lifecycle.ViewModel

open class BaseViewModel<T> : ViewModel() {
    private var navigator:T? = null

    fun setNavigator(navigator:T){
        this.navigator = navigator
        onNavigatorAttached()
    }

    protected open fun onNavigatorAttached() {}

    fun getNavigator() = navigator
}