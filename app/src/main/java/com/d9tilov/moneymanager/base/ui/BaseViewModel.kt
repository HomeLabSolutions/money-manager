package com.d9tilov.moneymanager.base.ui

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel<T> : ViewModel() {

    var navigator: T? = null
        set(value) {
            field = value
            field?.let { onNavigatorAttached() }
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