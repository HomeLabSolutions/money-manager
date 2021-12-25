package com.d9tilov.moneymanager.category.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseDialogFragment
import com.d9tilov.moneymanager.base.ui.navigator.CategoryUnionDialogNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.exception.CategoryExistException
import com.d9tilov.moneymanager.category.ui.vm.CategoryUnionViewModel
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.onChange
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.databinding.FragmentDialogCategoryUnionBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryUnitDialog :
    BaseDialogFragment<CategoryUnionDialogNavigator>(),
    CategoryUnionDialogNavigator {

    private val args by navArgs<CategoryUnitDialogArgs>()
    private val firstCategory by lazy { args.firstCategory }
    private val secondCategory by lazy { args.secondCategory }
    private val transactionType by lazy { args.transactionType }
    private val viewBinding by viewBinding(FragmentDialogCategoryUnionBinding::bind)

    override val layoutId = R.layout.fragment_dialog_category_union
    override fun getNavigator() = this
    override val viewModel by viewModels<CategoryUnionViewModel> {
        CategoryUnionViewModel.provideFactory(
            categoryUnionViewModelFactory,
            this,
            arguments
        )
    }

    @Inject
    lateinit var categoryUnionViewModelFactory: CategoryUnionViewModel.CategoryUnionViewModelFactory

    override fun accept() {
        dismiss()
    }

    override fun showError(error: Throwable) {
        if (error is CategoryExistException) {
            viewBinding.categoryDialogUnionEtNameLayout.error =
                getString(R.string.category_unit_name_exist_error)
        }
    }

    override fun cancel() {
        dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.run {
            categoryDialogUnionCancel.setOnClickListener { viewModel.cancel() }
            val firstDrawable =
                createTintDrawable(requireContext(), firstCategory.icon, firstCategory.color)
            categoryDialogUnionFolder.categoryDialogUnionItem1.setImageDrawable(firstDrawable)
            if (secondCategory.children.isNotEmpty()) {
                categoryDialogUnionTitle.text = getString(R.string.category_add_to_group_title)
                categoryDialogUnionSubtitle.text = getString(
                    R.string.category_unit_to_group_subtitle,
                    firstCategory.name,
                    secondCategory.name
                )
                categoryDialogUnionEtNameLayout.gone()
                categoryDialogUnionConfirm.text = getString(R.string.add)
                categoryDialogCurrentFolder.categoryDialogUnionItem1.setImageDrawable(
                    firstDrawable
                )
                categoryDialogCurrentFolder.root.show()
            } else {
                categoryDialogUnionConfirm.isEnabled = categoryDialogUnionEtName.length() > 0
                categoryDialogUnionSubtitle.text = getString(R.string.category_unit_new_name_subtitle)
                categoryDialogUnionTitle.text = getString(R.string.category_unit_to_group_title)
                categoryDialogUnionFolder.categoryDialogUnionItem1.setImageDrawable(firstDrawable)
                val secondDrawable = createTintDrawable(requireContext(), secondCategory.icon, secondCategory.color)
                categoryDialogUnionFolder.categoryDialogUnionItem2.setImageDrawable(secondDrawable)
                categoryDialogUnionConfirm.text = getString(R.string.save)
                categoryDialogUnionFolder.root.show()
            }
            categoryDialogUnionConfirm.setOnClickListener {
                if (secondCategory.children.isEmpty() &&
                    viewBinding.categoryDialogUnionEtName.text.toString().isEmpty()
                ) {
                    showError(IllegalArgumentException())
                } else if (secondCategory.children.isNotEmpty()) {
                    viewModel.addToGroup(firstCategory, secondCategory)
                } else {
                    viewModel.createGroup(
                        firstCategory,
                        secondCategory,
                        createUnionCategory()
                    )
                }
            }
            categoryDialogUnionEtName.onChange { text ->
                val isNameEmpty = text.isEmpty()
                viewBinding.categoryDialogUnionConfirm.isEnabled = !isNameEmpty
                viewBinding.categoryDialogUnionEtNameLayout.error = null
            }
        }
    }

    private fun createUnionCategory(): Category {
        return Category.EMPTY.copy(
            type = transactionType,
            name = viewBinding.categoryDialogUnionEtName.text.toString(),
            color = R.color.category_all_color,
            icon = R.drawable.ic_category_folder
        )
    }

    override fun onStart() {
        super.onStart()
        if (secondCategory.children.isEmpty()) {
            showKeyboard(viewBinding.categoryDialogUnionEtName)
        }
    }
}
