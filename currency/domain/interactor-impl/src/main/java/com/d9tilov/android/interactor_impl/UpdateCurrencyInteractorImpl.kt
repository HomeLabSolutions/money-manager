package com.d9tilov.android.interactor_impl

import com.d9tilov.android.interactor.CurrencyInteractor
import com.d9tilov.android.interactor.UpdateCurrencyInteractor

class UpdateCurrencyInteractorImpl(
    private val currencyInteractor: CurrencyInteractor,
) : UpdateCurrencyInteractor {

    override suspend fun updateCurrency(code: String) {
        currencyInteractor.updateMainCurrency(code)
    }
}
