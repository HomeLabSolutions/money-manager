package com.d9tilov.moneymanager.core.ui

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d9tilov.moneymanager.core.AndroidDisposable
import com.d9tilov.moneymanager.core.network.NetworkRetryManager

abstract class BaseViewModel<T : BaseNavigator> : ViewModel() {

    var navigator: T? = null
        set(value) {
            field = value
            field?.let { onNavigatorAttached() }
        }
    private val msg = MutableLiveData<Int>()
    private val loading = MutableLiveData<Boolean>()
    protected val compositeDisposable = AndroidDisposable()
    protected val retryManager = NetworkRetryManager()

    fun retryCall() {
        retryManager.retry()
    }

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

    fun getMsg():LiveData<Int> = msg
    fun getLoading():LiveData<Boolean> = loading

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
