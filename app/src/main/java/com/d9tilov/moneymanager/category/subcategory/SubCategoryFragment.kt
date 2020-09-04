package com.d9tilov.moneymanager.category.subcategory

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.SimpleItemAnimator
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.subcategory.vm.SubCategoryViewModel
import com.d9tilov.moneymanager.incomeexpense.ui.IncomeExpenseFragment
import com.d9tilov.moneymanager.transaction.TransactionType
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
    private val transactionType by lazy { args.transactionType }

    override fun getNavigator() = this
    override val viewModel by viewModels<SubCategoryViewModel>()

    private lateinit var modifiedParentCategory: Category
    @Inject lateinit var firebaseAnalytics: FirebaseAnalytics

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
        viewBinding?.let {
            it.categoryGroupEdit.visibility = VISIBLE
            it.categoryGroupDelete.visibility = VISIBLE
            it.categoryCreate.visibility = GONE
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
            (it.categoryRv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
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
        findNavController().getBackStackEntry(R.id.income_expense_dest).savedStateHandle.set(
            IncomeExpenseFragment.ARG_TRANSACTION_CREATED,
            true
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

    override fun backToFixedTransactionCreationScreen(category: Category) {
        findNavController().getBackStackEntry(
            if (destination == CategoryDestination.PREPOPULATE_SCREEN)
                R.id.fixed_created_transaction_dest else
                R.id.edit_transaction_dest
        ).savedStateHandle.set(
            ARG_CATEGORY,
            category
        )
        findNavController().popBackStack(R.id.category_dest, true)
    }

    override fun openRemoveDialog(subCategory: Category) {
        val action = SubCategoryFragmentDirections.toRemoveSubCategoryDialog(subCategory)
        findNavController().navigate(action)
    }

    companion object {
        const val SUB_CATEGORY_TITLE = "subcategory_title"
    }
}
