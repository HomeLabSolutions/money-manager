package com.d9tilov.android.category.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.d9tilov.android.category.data.model.Category
import com.d9tilov.android.category.ui.navigation.CategoryUnionDialogNavigator
import com.d9tilov.android.category.ui.vm.CategoryUnionViewModel
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.category_ui.databinding.FragmentDialogCategoryUnionBinding
import com.d9tilov.android.common_android.ui.base.BaseDialogFragment
import com.d9tilov.android.common_android.utils.createTintDrawable
import com.d9tilov.android.common_android.utils.gone
import com.d9tilov.android.common_android.utils.let2
import com.d9tilov.android.common_android.utils.onChange
import com.d9tilov.android.common_android.utils.show
import com.d9tilov.android.common_android.utils.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryUnitDialog :
    BaseDialogFragment<CategoryUnionDialogNavigator, FragmentDialogCategoryUnionBinding>(FragmentDialogCategoryUnionBinding::inflate),
    CategoryUnionDialogNavigator {

    private val args by navArgs<CategoryUnitDialogArgs>()
    private val firstCategory by lazy { args.firstCategory }
    private val secondCategory by lazy { args.secondCategory }
    private val transactionType by lazy { args.transactionType }

    override fun getNavigator() = this
    override val viewModel by viewModels<CategoryUnionViewModel>()

    override fun accept() {
        dismiss()
    }

    override fun showError(error: Throwable) {
        if (error is com.d9tilov.android.category.data.model.exception.CategoryException.CategoryExistException) {
            viewBinding?.categoryDialogUnionEtNameLayout?.error =
                getString(R.string.category_unit_name_exist_error)
        }
    }

    override fun cancel() {
        dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            categoryDialogUnionCancel.setOnClickListener { viewModel.cancel() }
            val firstDrawable = let2(firstCategory.icon, firstCategory.color) { icon, color ->
                createTintDrawable(requireContext(), icon, color)
            }
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
                categoryDialogUnionConfirm.isEnabled = categoryDialogUnionEtName.text.toString().isNotEmpty()
                categoryDialogUnionSubtitle.text = getString(R.string.category_unit_new_name_subtitle)
                categoryDialogUnionTitle.text = getString(R.string.category_unit_to_group_title)
                categoryDialogUnionFolder.categoryDialogUnionItem1.setImageDrawable(firstDrawable)
                val secondDrawable = let2(secondCategory.icon, secondCategory.color) { icon, color ->
                    createTintDrawable(requireContext(), icon, color)
                }
                categoryDialogUnionFolder.categoryDialogUnionItem2.setImageDrawable(secondDrawable)
                categoryDialogUnionConfirm.text = getString(R.string.save)
                categoryDialogUnionFolder.root.show()
            }
            categoryDialogUnionConfirm.setOnClickListener {
                if (secondCategory.children.isEmpty() && categoryDialogUnionEtName.text.toString().isEmpty()) {
                    showError(com.d9tilov.android.category.data.model.exception.CategoryException.CategoryEmptyNameException())
                } else if (secondCategory.children.isNotEmpty()) {
                    viewModel.addToGroup(firstCategory, secondCategory)
                } else viewModel.createGroup(firstCategory, secondCategory, createUnionCategory())
            }
            categoryDialogUnionEtName.onChange { text ->
                val isNameEmpty = text.isEmpty()
                categoryDialogUnionConfirm.isEnabled = !isNameEmpty
                categoryDialogUnionEtNameLayout.error = null
            }
        }
    }

    private fun createUnionCategory(): Category {
        return Category.EMPTY_EXPENSE.copy(
            type = transactionType,
            name = viewBinding?.categoryDialogUnionEtName?.text.toString(),
            color = R.color.category_all_color,
            icon = R.drawable.ic_category_folder
        )
    }

    override fun onStart() {
        super.onStart()
        if (secondCategory.children.isEmpty()) {
            showKeyboard(viewBinding?.categoryDialogUnionEtName)
        }
    }
}
