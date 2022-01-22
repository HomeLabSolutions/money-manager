package com.d9tilov.moneymanager.category.subcategory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.SimpleItemAnimator
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.subcategory.vm.SubCategoryViewModel
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.incomeexpense.ui.IncomeExpenseFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SubCategoryFragment :
    BaseCategoryFragment<SubCategoryNavigator>(),
    SubCategoryNavigator {

    private val args by navArgs<SubCategoryFragmentArgs>()
    private val parentCategory by lazy { args.parentCategory }
    private lateinit var modifiedParentCategory: Category

    override fun getNavigator() = this
    override val viewModel by viewModels<SubCategoryViewModel> {
        SubCategoryViewModel.provideFactory(
            subCategoryViewModelFactory,
            this,
            arguments
        )
    }

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    @Inject
    lateinit var subCategoryViewModelFactory: SubCategoryViewModel.SubCategoryViewModelFactory

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
            findNavController().previousBackStackEntry?.savedStateHandle?.remove<Category>(
                SUB_CATEGORY_TITLE
            )
        }
        viewBinding.run {
            categoryGroupEdit.show()
            categoryGroupDelete.show()
            categoryCreate.gone()
            categoryGroupDelete.setOnClickListener {
                val action = SubCategoryFragmentDirections.toRemoveCategoryDialog(
                    CategoryDestination.SUB_CATEGORY_SCREEN, modifiedParentCategory
                )
                findNavController().navigate(action)
            }
            categoryGroupEdit.setOnClickListener {
                val action = SubCategoryFragmentDirections.toEditCategoryDialog(
                    modifiedParentCategory
                )
                findNavController().navigate(action)
            }
            (categoryRv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    override fun backToEditTransactionScreen(category: Category) {
        findNavController().getBackStackEntry(R.id.edit_transaction_dest).savedStateHandle.set(
            ARG_CATEGORY,
            category
        )
        findNavController().popBackStack(R.id.category_dest, true)
    }

    override fun backToMainScreen(category: Category) {
        findNavController().getBackStackEntry(R.id.income_expense_dest).savedStateHandle.set(
            IncomeExpenseFragment.ARG_TRANSACTION_CREATED,
            category
        )
        findNavController().popBackStack(R.id.category_dest, true)
    }

    override fun openCreateCategoryScreen(category: Category?) {
        val action = SubCategoryFragmentDirections.toCategoryCreationDest(
            transactionType,
            category
        )
        findNavController().navigate(action)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.ITEM_ID, "open_creation_category_screen")
        }
    }

    override fun openRemoveDialog(subCategory: Category) {
        val action = SubCategoryFragmentDirections.toRemoveSubCategoryDialog(subCategory)
        findNavController().navigate(action)
    }

    companion object {
        const val SUB_CATEGORY_TITLE = "subcategory_title"
    }
}
