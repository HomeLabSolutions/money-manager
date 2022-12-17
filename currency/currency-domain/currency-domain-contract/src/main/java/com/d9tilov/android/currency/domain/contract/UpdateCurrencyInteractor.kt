package com.d9tilov.android.currency.domain.contract

interface UpdateCurrencyInteractor {

    suspend fun updateCurrency(code: String)
}
