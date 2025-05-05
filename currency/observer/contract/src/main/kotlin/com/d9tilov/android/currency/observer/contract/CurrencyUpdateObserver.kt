package com.d9tilov.android.currency.observer.contract

interface CurrencyUpdateObserver {
    suspend fun updateMainCurrency(code: String)
}
