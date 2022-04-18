package com.d9tilov.moneymanager.base.data

sealed class ResultOf<out T> {
    data class Success<out R>(val data: R) : ResultOf<R>()
    class Loading<out R> : ResultOf<R>()
    data class Failure(
        val throwable: Throwable?,
        val message: String? = null
    ) : ResultOf<Nothing>()
}
