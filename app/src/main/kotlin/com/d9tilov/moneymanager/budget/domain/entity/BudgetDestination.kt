package com.d9tilov.moneymanager.budget.domain.entity

sealed class BudgetDestination {

    object PrepopulateScreen : BudgetDestination()

    object ProfileScreen : BudgetDestination()
}
