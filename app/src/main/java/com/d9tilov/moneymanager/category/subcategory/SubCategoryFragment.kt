package com.d9tilov.moneymanager.category.subcategory

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.subcategory.vm.SubCategoryViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.ui.EditTransactionFragment.Companion.ARG_CATEGORY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubCategoryFragment :
    BaseCategoryFragment<SubCategoryNavigator>(),
    SubCategoryNavigator {

    private val args by navArgs<SubCategoryFragmentArgs>()
    private val parentCategory by lazy { args.parentCategory }
    private val transactionType by lazy { args.transactionType }

    override fun getNavigator() = this
    override val viewModel by viewModels<SubCategoryViewModel>()

    private lateinit var modifiedParentCategory: Category

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modifiedParentCategory = parentCategory
        toolbar?.title = getString(R.string.title_sub_category, modifiedParentCategory.name)
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Category>(
            SUB_CATEGORY_TITLE
        )?.observe(
            viewLifecycleOwner
        ) {
            modifiedParentCategory = it
            toolbar?.title = getString(R.string.title_sub_category, it.name)
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Category>(
                SUB_CATEGORY_TITLE
            )
        }
        viewBinding?.let {
            it.categoryGroupEdit.visibility = VISIBLE
            it.categoryGroupDelete.visibility = VISIBLE
            it.categoryGroupDelete.setOnClickListener {
                val action = SubCategoryFragmentDirections.toRemoveCategoryDialog(
                    CategoryDestination.SUB_CATEGORY_SCREEN, modifiedParentCategory
                )
                findNavController().navigate(action)
            }
            it.categoryGroupEdit.setOnClickListener {
                val action = SubCategoryFragmentDirections.toEditCategoryDialog(
                    modifiedParentCategory
                )
                findNavController().navigate(action)
            }
        }
    }

    override fun backToEditTransactionScreen(category: Category) {
        findNavController().getBackStackEntry(R.id.edit_transaction_dest).savedStateHandle.set(
            ARG_CATEGORY,
            category
        )
        findNavController().popBackStack(R.id.category_dest, true)
    }

    override fun backToMainScreen(transactionType: TransactionType) {
        findNavController().popBackStack(R.id.category_dest, true)
    }

    override fun openCreateCategoryScreen(category: Category?) {
        val action = SubCategoryFragmentDirections.toCategoryCreationDest(
            transactionType,
            category
        )
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
