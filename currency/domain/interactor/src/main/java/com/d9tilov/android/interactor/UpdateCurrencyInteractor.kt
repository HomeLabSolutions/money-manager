package com.d9tilov.android.interactor

import com.d9tilov.android.core.model.Interactor

interface UpdateCurrencyInteractor : Interactor {

    suspend fun updateCurrency(code: String)
}
