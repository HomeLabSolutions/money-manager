package com.d9tilov.moneymanager.core.ui

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d9tilov.moneymanager.core.AndroidDisposable

abstract class BaseViewModel<T : BaseNavigator> : ViewModel() {

    var navigator: T? = null
        set(value) {
            field = value
            field?.let { onNavigatorAttached() }
        }
    val msg = MutableLiveData<Int>()
    val loading = MutableLiveData<Boolean>()
    protected val compositeDisposable = AndroidDisposable()

    // Post in background thread
    fun postMessage(@StringRes message: Int) {
        msg.postValue(message)
    }

    // Post in main thread
    fun setMessage(@StringRes message: Int) {
        msg.value = message
    }

    fun setLoading(show: Boolean) {
        loading.value = show
    }

    fun postLoading(show: Boolean) {
        loading.postValue(show)
    }

    protected open fun onNavigatorAttached() {}

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
