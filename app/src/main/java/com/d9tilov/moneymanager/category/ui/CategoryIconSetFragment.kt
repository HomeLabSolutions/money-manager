package com.d9tilov.moneymanager.category.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.CategorySetNavigator
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.ui.recycler.CategoryIconSetAdapter
import com.d9tilov.moneymanager.category.ui.vm.CategorySetViewModel
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.ui.recyclerview.GridSpaceItemDecoration
import com.d9tilov.moneymanager.databinding.FragmentCategoryIconSetBinding
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryIconSetFragment :
    BaseFragment<FragmentCategoryIconSetBinding, CategorySetNavigator>(R.layout.fragment_category_icon_set),
    CategorySetNavigator {

    private val args by navArgs<CategoryIconSetFragmentArgs>()
    private val transactionType by lazy { args.transactionType }
    private val destination by lazy { args.destination }

    private var toolbar: MaterialToolbar? = null
    private lateinit var categoryAdapter: CategoryIconSetAdapter

    override fun performDataBinding(view: View) = FragmentCategoryIconSetBinding.bind(view)
    override fun getNavigator() = this
    override val viewModel by viewModels<CategorySetViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoryAdapter = CategoryIconSetAdapter()
        categoryAdapter.itemClickListener = onItemIconClickListener
    }

    private val onItemIconClickListener = object : OnItemClickListener<Int> {
        override fun onItemClick(item: Int, position: Int) {
            viewModel.save(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.let {
            val layoutManager =
                GridLayoutManager(requireContext(), SPAN_COUNT, GridLayoutManager.VERTICAL, false)
            it.categorySetRvCategory.layoutManager = layoutManager
            it.categorySetRvCategory.adapter = categoryAdapter
            it.categorySetRvCategory.addItemDecoration(
                GridSpaceItemDecoration(
                    spanCount = SPAN_COUNT,
                    spacing = resources.getDimension(R.dimen.recycler_view_category_offset).toInt()
                )
            )
        }
        toolbar = viewBinding?.categorySetToolbarContainer?.toolbar
        initToolbar(toolbar)
    }

    private fun initToolbar(toolbar: Toolbar?) {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.title = getString(R.string.title_category_set)
    }

    override fun save(category: Category) {
        val action =
            CategoryIconSetFragmentDirections.toCategoryCreationDest(category, destination, transactionType)
        findNavController().navigate(action)
    }

    companion object {
        private const val SPAN_COUNT = 4
    }
}