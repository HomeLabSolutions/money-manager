package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.base.domain.Interactor

interface UpdateCurrencyInteractor : Interactor {

    suspend fun updateCurrency(code: String)
}
