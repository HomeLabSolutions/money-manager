package com.d9tilov.android.category.domain.model.exception

sealed class CategoryException(message: String) : Exception(message) {

    data class CategoryEmptyNameException(val errorMessage: String = "Category doesn't have a name") : CategoryException(errorMessage)
    data class CategoryExistException(val errorMessage: String = "Category already exists") : CategoryException(errorMessage)
    data class CategoryNotFoundException(val errorMessage: String = "Category not found") : CategoryException(errorMessage)
    data class CategoryNoParentException(val errorMessage: String = "Category's parent not found") : CategoryException(errorMessage)
}
