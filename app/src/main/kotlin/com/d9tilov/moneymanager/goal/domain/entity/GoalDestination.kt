package com.d9tilov.moneymanager.goal.domain.entity

sealed class GoalDestination {

    object PrepopulateScreen : GoalDestination()

    object ProfileScreen : GoalDestination()
}
