package com.d9tilov.moneymanager.category.domain.entity

sealed class CategoryDestination {

    object MainScreen : CategoryDestination()

    object MainWithSumScreen : CategoryDestination()

    object EditTransactionScreen : CategoryDestination()

    object EditRegularTransactionScreen : CategoryDestination()

    object CategoryCreationScreen : CategoryDestination()

    object CategoryScreen : CategoryDestination()

    object SubCategoryScreen : CategoryDestination()
}
