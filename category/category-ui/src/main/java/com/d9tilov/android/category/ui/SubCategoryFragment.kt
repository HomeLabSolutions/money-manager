package com.d9tilov.android.category.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.SimpleItemAnimator
import com.d9tilov.android.category.data.model.Category
import com.d9tilov.android.category.ui.navigation.CategoryDestination
import com.d9tilov.android.category.ui.navigation.SubCategoryNavigator
import com.d9tilov.android.category.ui.vm.SubCategoryViewModel
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.common_android.utils.gone
import com.d9tilov.android.common_android.utils.show
import com.d9tilov.android.core.constants.NavigationConstants.ARG_TRANSACTION_CREATED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubCategoryFragment : BaseCategoryFragment<SubCategoryNavigator>(), SubCategoryNavigator {

    private val args by navArgs<SubCategoryFragmentArgs>()
    private val parentCategory: Category by lazy { args.parentCategory }
    private var modifiedParentCategory: Category? = null

    override fun getNavigator() = this
    override val viewModel by viewModels<SubCategoryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modifiedParentCategory = parentCategory
        toolbar?.title = getString(R.string.title_sub_category, modifiedParentCategory?.name)
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Category>(
            SUB_CATEGORY_TITLE
        )?.observe(
            viewLifecycleOwner
        ) {
            modifiedParentCategory = it
            toolbar?.title = getString(R.string.title_sub_category, it.name)
            findNavController().previousBackStackEntry?.savedStateHandle?.remove<Category>(
                SUB_CATEGORY_TITLE
            )
        }
        viewBinding?.run {
            categoryGroupEdit.show()
            categoryGroupDelete.show()
            categoryCreate.gone()
            categoryGroupDelete.setOnClickListener {
                val action = SubCategoryFragmentDirections.toRemoveCategoryDialog(
                    CategoryDestination.SubCategoryScreen, modifiedParentCategory!!
                )
                findNavController().navigate(action)
            }
            categoryGroupEdit.setOnClickListener {
                val action =
                    SubCategoryFragmentDirections.toEditCategoryDialog(modifiedParentCategory!!)
                findNavController().navigate(action)
            }
            (categoryRv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    override fun backToEditTransactionScreen(category: Category) {
        findNavController().getBackStackEntry(R.id.edit_transaction_dest).savedStateHandle.set(
            ARG_CATEGORY, category
        )
        findNavController().popBackStack(R.id.category_dest, true)
    }

    override fun backToEditRegularTransactionScreen(category: Category) {
        val regularTransactionIdentifier = resources.getIdentifier(
            "regular_transaction_nav_graph", "id", requireContext().packageName
        )
        findNavController().getBackStackEntry(regularTransactionIdentifier).savedStateHandle[ARG_CATEGORY] =
            category
        findNavController().popBackStack(R.id.category_dest, true)
    }

    override fun backToMainScreen(category: Category) {
        val incomeExpenseIdentifier = resources.getIdentifier(
            "income_expense_nav_graph", "id", requireContext().packageName
        )
        findNavController().getBackStackEntry(incomeExpenseIdentifier).savedStateHandle[ARG_TRANSACTION_CREATED] =
            category
        findNavController().popBackStack(R.id.category_dest, true)
    }

    override fun openCreateCategoryScreen(category: Category) {
        val action = SubCategoryFragmentDirections.toCategoryCreationDest(category)
        findNavController().navigate(action)
    }

    override fun openRemoveDialog(subCategory: Category) {
        val action = SubCategoryFragmentDirections.toRemoveSubCategoryDialog(subCategory)
        findNavController().navigate(action)
    }

    companion object {
        const val SUB_CATEGORY_TITLE = "subcategory_title"
    }
}
