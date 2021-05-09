package com.d9tilov.moneymanager.base.data

data class Resource<out T>(val status: Status, val data: T? = null, val message: String? = null) {

    companion object {

        fun <T> success(data: T? = null): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(exception: Exception, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, exception.message)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}
