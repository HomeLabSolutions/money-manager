package com.d9tilov.moneymanager.base.ui

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
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

    private val _message = MutableLiveData<Event<Int>>()
    val message : LiveData<Event<Int>>
        get() = _message

    // Post in background thread
    fun postMessage(@StringRes message: Int) {
        _message.postValue(Event(message))
    }

    // Post in main thread
    fun setMessage(@StringRes message: Int) {
        _message.value = Event(message)
    }

    private val compositeDisposable = CompositeDisposable()

    protected open fun onNavigatorAttached() {}
    protected fun subscribe(event: Disposable) = compositeDisposable.add(event)

    override fun onCleared() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        super.onCleared()
    }
}