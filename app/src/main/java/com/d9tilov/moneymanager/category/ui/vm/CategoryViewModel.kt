package com.d9tilov.moneymanager.category.ui.vm

import com.d9tilov.moneymanager.base.ui.BaseViewModel
import com.d9tilov.moneymanager.base.ui.navigator.CategoryNavigator
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.domain.ICategoryInteractor
import javax.inject.Inject

class CategoryViewModel @Inject constructor(private val categoryInteractor: ICategoryInteractor) : BaseViewModel<CategoryNavigator>()
