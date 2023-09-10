package com.d9tilov.android.common.android.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<T : BaseNavigator> : ViewModel() {

    var navigator: T? = null
        set(value) {
            field = value
            field?.let { onNavigatorAttached() }
        }
    private val msg = MutableSharedFlow<Int>()
    private val loading = MutableStateFlow(false)

    suspend fun setMessage(@StringRes message: Int) {
        msg.emit(message)
    }

    fun setLoading(show: Boolean) {
        loading.value = show
    }

    protected open fun onNavigatorAttached() {}

    fun getMsg(): SharedFlow<Int> = msg
    fun getLoading(): StateFlow<Boolean> = loading

    companion object {
        const val DEFAULT_MESSAGE_ID = -1
    }
}
