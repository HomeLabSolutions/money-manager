package com.d9tilov.android.category.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.d9tilov.android.category.domain.model.CategoryGroup
import com.d9tilov.android.category.ui.navigation.CategorySetNavigator
import com.d9tilov.android.category.ui.recycler.CategoryIconsSetAdapter
import com.d9tilov.android.category.ui.vm.CategorySetViewModel
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.category_ui.databinding.FragmentCategoryIconSetBinding
import com.d9tilov.android.common_android.ui.base.BaseFragment
import com.d9tilov.android.common_android.ui.recyclerview.GridSpaceItemDecoration
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryIconSetFragment :
    BaseFragment<CategorySetNavigator, FragmentCategoryIconSetBinding>(
        FragmentCategoryIconSetBinding::inflate,
        R.layout.fragment_category_icon_set
    ), CategorySetNavigator {
    private val args by navArgs<CategoryIconSetFragmentArgs>()
    private val groupId: CategoryGroup by lazy { args.groupId }
    private val title: Int by lazy { if (args.title.toLong() == NO_ID) R.string.title_category_set else args.title }

    private var toolbar: MaterialToolbar? = null
    private val categoryAdapter: CategoryIconsSetAdapter by lazy {
        val list = viewModel.getIconsByGroupId(groupId)
        CategoryIconsSetAdapter(list) { item, _ -> viewModel.save(item) }
    }

    override fun getNavigator() = this

    override val viewModel by viewModels<CategorySetViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            val layoutManager =
                GridLayoutManager(
                    requireContext(),
                    SPAN_COUNT,
                    GridLayoutManager.VERTICAL,
                    false
                )
            categorySetRvCategory.layoutManager = layoutManager
            categorySetRvCategory.adapter = categoryAdapter
            categorySetRvCategory.addItemDecoration(
                GridSpaceItemDecoration(
                    spanCount = SPAN_COUNT,
                    spacing = resources.getDimension(R.dimen.recycler_view_category_offset)
                        .toInt()
                )
            )
            toolbar = categorySetToolbarContainer.toolbar
        }
        initToolbar(toolbar)
    }

    private fun initToolbar(toolbar: Toolbar?) {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.title = getString(this.title)
    }

    override fun save(icon: Int) {
        findNavController().getBackStackEntry(R.id.category_creation_dest).savedStateHandle.set(
            ARG_CATEGORY_ICON_ID,
            icon
        )
        if (viewModel.isPremium) findNavController().popBackStack(
            R.id.grouped_category_set_dest,
            true
        )
        else findNavController().popBackStack()
    }

    companion object {
        private const val SPAN_COUNT = 4
        const val ARG_CATEGORY_ICON_ID = "arg_category_icon_id"
    }
}
