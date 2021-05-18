package com.d9tilov.moneymanager.profile.domain

import com.d9tilov.moneymanager.base.domain.Interactor

interface ProfileInteractor : Interactor {

    fun saveNumber()
    fun restoreNumber(): Int
}
