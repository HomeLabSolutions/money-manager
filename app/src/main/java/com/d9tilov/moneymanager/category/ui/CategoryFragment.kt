package com.d9tilov.moneymanager.category.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.CategoryNavigator
import com.d9tilov.moneymanager.base.ui.recyclerview.SpaceItemDecoration
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.subcategory.SubCategoryFragment.Companion.ARG_SUB_CATEGORY
import com.d9tilov.moneymanager.category.ui.vm.CategoryViewModel
import com.d9tilov.moneymanager.core.util.events.OnItemClickListener
import com.d9tilov.moneymanager.databinding.FragmentCategoryBinding

class CategoryFragment :
    BaseFragment<FragmentCategoryBinding, CategoryNavigator, CategoryViewModel>(),
    CategoryNavigator {

    private lateinit var categoryAdapter: CategoryModifyAdapter
    private lateinit var toolbar: Toolbar

    override fun getLayoutId() = R.layout.fragment_category
    override fun performDataBinding(view: View): FragmentCategoryBinding =
        FragmentCategoryBinding.bind(view)

    override fun getViewModelClass() = CategoryViewModel::class.java
    override fun getNavigator() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter = CategoryModifyAdapter()
        categoryAdapter.itemClickListener = onItemClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
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
        viewModel.categories.observe(
            this.viewLifecycleOwner,
            Observer { categoryAdapter.updateItems(it) })
    }

    private fun initToolbar() {
        toolbar = viewBinding.categoryToolbarContainer.toolbar
        toolbar.title = getString(R.string.title_category)
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    private val onItemClickListener = object : OnItemClickListener<Category> {
        override fun onItemClick(item: Category, position: Int) {
            viewModel.onCategoryClicked(item)
        }
    }

    override fun openSubCategoryScreen(category: Category) {
        val bundle = bundleOf(ARG_SUB_CATEGORY to category)
        findNavController().navigate(R.id.action_mainFragment_to_sub_category_dest, bundle)
    }

    override fun openCreateCategoryDialog() {
        TODO("Not yet implemented")
    }

    companion object {
        fun create() = CategoryFragment()
    }
}
