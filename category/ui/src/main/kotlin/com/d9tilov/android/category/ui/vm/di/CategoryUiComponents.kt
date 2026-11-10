package com.d9tilov.android.category.ui.vm.di

import com.d9tilov.android.category.ui.vm.CategoryCreationViewModel
import com.d9tilov.android.category.ui.vm.CategoryGroupIconListViewModel
import com.d9tilov.android.category.ui.vm.CategoryIconGridViewModel
import com.d9tilov.android.category.ui.vm.CategoryListViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val categoryUiComponents =
    module {
        viewModel<CategoryIconGridViewModel>()
        viewModel<CategoryListViewModel>()
        viewModel<CategoryCreationViewModel>()
        viewModel<CategoryGroupIconListViewModel>()
    }
