package com.d9tilov.android.currency.domain.impl

import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.contract.UpdateCurrencyInteractor

class UpdateCurrencyInteractorImpl(
    private val currencyInteractor: CurrencyInteractor,
) : UpdateCurrencyInteractor {

    override suspend fun updateCurrency(code: String) {
        currencyInteractor.updateMainCurrency(code)
    }
}
