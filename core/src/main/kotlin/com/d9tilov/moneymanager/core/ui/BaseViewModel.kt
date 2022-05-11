package com.d9tilov.moneymanager.core.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_DATA_ID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<T : BaseNavigator> : ViewModel() {

    var navigator: T? = null
        set(value) {
            field = value
            field?.let { onNavigatorAttached() }
        }
    private val msg = MutableStateFlow(DEFAULT_MESSAGE_ID)
    private val loading = MutableStateFlow(false)

    fun setMessage(@StringRes message: Int) {
        msg.value = message
    }

    fun setLoading(show: Boolean) {
        loading.value = show
    }

    protected open fun onNavigatorAttached() {}

    fun getMsg(): StateFlow<Int> = msg
    fun getLoading(): StateFlow<Boolean> = loading

    companion object {
        const val DEFAULT_MESSAGE_ID = -1
    }
}
