package com.d9tilov.moneymanager.category

class CategoryInteractor(private val categoryRepo: ICategoryRepo) :
    ICategoryInteractor {
    override fun createDefaultCategories() = categoryRepo.createDefaultCategories()
}
