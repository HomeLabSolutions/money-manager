package com.d9tilov.android.network.dispatchers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val moneyManagerDispatcher: MoneyManagerDispatchers)

enum class MoneyManagerDispatchers {
    Default,
    IO,
    Main
}
