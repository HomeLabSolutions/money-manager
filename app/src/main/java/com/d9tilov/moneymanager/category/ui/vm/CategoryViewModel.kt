package com.d9tilov.moneymanager.category.ui.vm

import android.util.Log
import com.d9tilov.moneymanager.base.ui.navigator.CategoryNavigator
import com.d9tilov.moneymanager.category.common.BaseCategoryViewModel
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import io.reactivex.Observable

class CategoryViewModel(private val categoryInteractor: CategoryInteractor) :
    BaseCategoryViewModel<CategoryNavigator>() {

    init {
        categoryInteractor.getAllCategories()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { categories.value = it }
            .addTo(compositeDisposable)
    }

    override fun onCategoryClicked(category: Category) {
        if (category.children.isNotEmpty()) {
            navigator?.openSubCategoryScreen(category)
        }
    }

    fun onCategoryLongClicked() {}

    override fun onCategoryRemoved(category: Category) {
        categoryInteractor.deleteCategory(category)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe()
            .addTo(compositeDisposable)
    }

    override fun onItemSwap(categories: List<Category>) {
        Log.d("moggot", "onItemSwap: ")
        val updatedList = mutableListOf<Category>()
        for ((index, value) in categories.withIndex()) {
            updatedList.add(value.copy(ordinal = index))
        }
        Observable.fromIterable(updatedList)
            .flatMapCompletable { category -> categoryInteractor.create(category) }
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe()
            .addTo(compositeDisposable)
    }

    override fun update(name: String) {
        categoryInteractor.update(categories.value!![0].copy(name = name))
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe()
            .addTo(compositeDisposable)
    }
}
