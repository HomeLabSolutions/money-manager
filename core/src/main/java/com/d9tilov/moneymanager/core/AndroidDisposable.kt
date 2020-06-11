package com.d9tilov.moneymanager.core

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class AndroidDisposable {

    private var compositeDisposable: CompositeDisposable? = null

    fun add(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(disposable)
    }

    fun clear() = compositeDisposable?.clear()

    fun dispose() {
        compositeDisposable?.dispose()
        compositeDisposable = null
    }
}
