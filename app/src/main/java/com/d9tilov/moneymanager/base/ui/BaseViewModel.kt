package com.d9tilov.moneymanager.base.ui

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel<T> : ViewModel() {

    var navigator: T? = null
        set(value) {
            field = value
            field?.let { onNavigatorAttached() }
        }

    val msg = MutableLiveData<Int>()
    val loading = MutableLiveData<Boolean>()

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

    private val compositeDisposable = CompositeDisposable()

    protected open fun onNavigatorAttached() {}
    protected fun unsubscribeOnDetach(event: Disposable) = compositeDisposable.add(event)

    override fun onCleared() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        super.onCleared()
    }
}
