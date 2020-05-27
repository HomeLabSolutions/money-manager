package com.d9tilov.moneymanager.category.ui

import android.view.View
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.CategoryNavigator
import com.d9tilov.moneymanager.category.ui.vm.CategoryViewModel
import com.d9tilov.moneymanager.databinding.FragmentCategoryBinding

class CategoryFragment : BaseFragment<FragmentCategoryBinding, CategoryViewModel>(), CategoryNavigator {
    override fun getLayoutId() = R.layout.fragment_category

    override fun performDataBinding(view: View): FragmentCategoryBinding = FragmentCategoryBinding.bind(view)
    override fun getViewModelClass() = CategoryViewModel::class.java

    override fun getNavigator() = this

    companion object {
        fun create() = CategoryFragment()
    }
}
