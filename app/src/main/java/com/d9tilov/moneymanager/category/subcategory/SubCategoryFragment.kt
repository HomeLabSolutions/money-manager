package com.d9tilov.moneymanager.category.subcategory

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
import com.d9tilov.moneymanager.base.ui.recyclerview.SpaceItemDecoration
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.subcategory.vm.SubCategoryViewModel
import com.d9tilov.moneymanager.category.ui.CategoryModifyAdapter
import com.d9tilov.moneymanager.databinding.FragmentCategoryBinding

class SubCategoryFragment :
    BaseFragment<FragmentCategoryBinding, SubCategoryNavigator, SubCategoryViewModel>(),
    SubCategoryNavigator {

    private lateinit var categoryAdapter: CategoryModifyAdapter
    private lateinit var toolbar: Toolbar

    override fun getLayoutId() = R.layout.fragment_category
    override fun performDataBinding(view: View): FragmentCategoryBinding =
        FragmentCategoryBinding.bind(view)

    override fun getViewModelClass(): Class<SubCategoryViewModel> = SubCategoryViewModel::class.java
    override fun getNavigator() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter = CategoryModifyAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.categories.observe(
            this.viewLifecycleOwner,
            Observer {
                categoryAdapter.updateItems(it)
                initToolbar(it[0].parent)
            })
        with(viewBinding) {
            categoryRv.layoutManager =
                GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
            categoryRv.adapter = categoryAdapter
            categoryRv.addItemDecoration(
                SpaceItemDecoration(
                    requireContext(),
                    R.dimen.recycler_view_category_offset
                )
            )
        }
    }

    private fun initToolbar(category: Category?) {
        toolbar = viewBinding.categoryToolbarContainer.toolbar
        toolbar.title = getString(R.string.title_sub_category, category?.name)
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun openCreateCategoryDialog() {
        TODO("Not yet implemented")
    }

    companion object {
        const val ARG_SUB_CATEGORY = "args_subcategory"
    }
}
