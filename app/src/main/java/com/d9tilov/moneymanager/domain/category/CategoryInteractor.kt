package com.d9tilov.moneymanager.domain.category

class CategoryInteractor(private val categoryRepo: CategoryRepo) : ICategoryInteractor {
    override fun createDefaultCategories() = categoryRepo.createDefaultCategories()
}