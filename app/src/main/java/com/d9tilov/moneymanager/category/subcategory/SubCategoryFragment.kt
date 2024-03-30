package com.d9tilov.moneymanager.category.subcategory

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.subcategory.vm.SubCategoryViewModel
import com.d9tilov.moneymanager.category.ui.CategoryFragment
import com.d9tilov.moneymanager.category.ui.CategoryModifyAdapter
import com.d9tilov.moneymanager.databinding.FragmentCategoryBinding

class SubCategoryFragment : BaseFragment<FragmentCategoryBinding, SubCategoryViewModel>(),
    SubCategoryNavigator {

    private lateinit var categoryAdapter: CategoryModifyAdapter
    private lateinit var toolbar: Toolbar

    override fun getLayoutId() = R.layout.fragment_category
    override fun performDataBinding(view: View): FragmentCategoryBinding =
        FragmentCategoryBinding.bind(view)

    override fun getViewModelClass(): Class<SubCategoryViewModel> = SubCategoryViewModel::class.java
    override fun getNavigator() = this

    private lateinit var parentCategory: Category

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getParcelable<Category>(ARG_SUB_CATEGORY)?.let {
            parentCategory = it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter = CategoryModifyAdapter()
        viewModel.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    private fun initToolbar() {
        toolbar = viewBinding.categoryToolbarContainer.toolbar
        toolbar.title = getString(R.string.title_sub_category, parentCategory.name)
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun openCreateCategoryDialog() {
        TODO("Not yet implemented")
    }

    companion object {
        fun create(category: Category) = CategoryFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_SUB_CATEGORY, category)
            }
        }

        const val ARG_SUB_CATEGORY = "arg_subcategory"
    }
}
