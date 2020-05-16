package com.d9tilov.moneymanager.incomeexpense.expense.ui

import com.d9tilov.moneymanager.backup.domain.BackupInteractor
import com.d9tilov.moneymanager.base.ui.BaseViewModel
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.settings.domain.SettingsInteractor
import javax.inject.Inject

class ExpenseViewModel @Inject constructor(private val backupInteractor: BackupInteractor) :
    BaseViewModel<ExpenseNavigator>() {

    var mainSum: StringBuilder = StringBuilder()
        private set

    fun updateNum(num: String) {
        mainSum = StringBuilder(num)
    }

    fun tapNum(num: String): String {
        mainSum.append(num)
        return mainSum.toString()
    }

    fun removeNum(): String {
        return if (mainSum.isNotEmpty() && mainSum.toString() != "0") {
            mainSum.deleteCharAt(mainSum.length - 1)
            if (mainSum.toString().isEmpty()) {
                "0"
            } else {
                mainSum.toString()
            }
        } else {
            "0"
        }
    }

    override fun onNavigatorAttached() {
        super.onNavigatorAttached()
        subscribe(
            backupInteractor.restoreDatabase()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe()
        )
    }
}