package com.d9tilov.android.category.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.d9tilov.android.category.domain.model.CategoryGroup
import com.d9tilov.android.category.domain.model.CategoryGroupItem
import com.d9tilov.android.category.ui.navigation.CategoryGroupSetNavigator
import com.d9tilov.android.category.ui.recycler.CategoryGroupIconSetAdapter
import com.d9tilov.android.category.ui.vm.CategoryGroupSetViewModel
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.category_ui.databinding.FragmentGroupCategoryIconSetBinding
import com.d9tilov.android.common.android.ui.base.BaseFragment
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryGroupIconSetFragment :
    BaseFragment<CategoryGroupSetNavigator, FragmentGroupCategoryIconSetBinding>(
        FragmentGroupCategoryIconSetBinding::inflate,
        R.layout.fragment_group_category_icon_set
    ),
    CategoryGroupSetNavigator {

    private val adapter: CategoryGroupIconSetAdapter = CategoryGroupIconSetAdapter(
        listOf(
            CategoryGroupItem(
                CategoryGroup.HOUSING,
                R.string.category_group_housing
            ),
            CategoryGroupItem(
                CategoryGroup.TRANSPORT,
                R.string.category_group_transportation
            ),
            CategoryGroupItem(
                CategoryGroup.FOOD,
                R.string.category_group_food
            ),
            CategoryGroupItem(
                CategoryGroup.UTILITIES,
                R.string.category_group_utilities
            ),
            CategoryGroupItem(
                CategoryGroup.INSURANCE,
                R.string.category_group_insurance
            ),
            CategoryGroupItem(
                CategoryGroup.MEDICAL,
                R.string.category_group_medical
            ),
            CategoryGroupItem(
                CategoryGroup.SPORT,
                R.string.category_group_sport
            ),
            CategoryGroupItem(
                CategoryGroup.INVESTING,
                R.string.category_group_investing
            ),
            CategoryGroupItem(
                CategoryGroup.RECREATION,
                R.string.category_group_recreation
            ),
            CategoryGroupItem(
                CategoryGroup.PERSONAL,
                R.string.category_group_personal
            ),
            CategoryGroupItem(
                CategoryGroup.OTHERS,
                R.string.category_group_others
            )
        )
    ) { item, _ -> viewModel.openGroupIconSet(item) }

    private var toolbar: MaterialToolbar? = null

    override fun getNavigator(): CategoryGroupSetNavigator = this

    override val viewModel by viewModels<CategoryGroupSetViewModel>()

    override fun openCategoryGroup(item: CategoryGroupItem) {
        val action = CategoryGroupIconSetFragmentDirections.toCategorySetDest(item.groupId, item.name)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            val layoutManager = LinearLayoutManager(requireContext())
            categoryGroupSetRvCategory.layoutManager = layoutManager
            categoryGroupSetRvCategory.adapter = adapter
            val dividerItemDecoration = DividerItemDecoration(
                categoryGroupSetRvCategory.context,
                layoutManager.orientation
            )
            val dividerDrawable = ContextCompat.getDrawable(requireContext(), com.d9tilov.android.designsystem.R.drawable.rv_divider)
            dividerDrawable?.let { divider -> dividerItemDecoration.setDrawable(divider) }
            categoryGroupSetRvCategory.addItemDecoration(dividerItemDecoration)
            toolbar = categoryGroupSetToolbarContainer.toolbar
        }
        initToolbar(toolbar)
    }

    private fun initToolbar(toolbar: Toolbar?) {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.title = getString(R.string.title_category_group_set)
    }
}
