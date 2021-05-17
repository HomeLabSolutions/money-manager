package com.d9tilov.moneymanager.base.data

data class Result<out T>(val status: Status, val data: T? = null, val message: String? = null) {

    companion object {

        fun <T> success(data: T? = null): Result<T> {
            return Result(Status.SUCCESS, data, null)
        }

        fun <T> error(exception: Exception, data: T? = null): Result<T> {
            return Result(Status.ERROR, data, exception.message)
        }

        fun <T> loading(data: T?): Result<T> {
            return Result(Status.LOADING, data, null)
        }
    }
}
