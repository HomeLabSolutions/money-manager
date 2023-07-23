package com.d9tilov.android.category.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.android.category.domain.model.exception.CategoryException
import com.d9tilov.android.category.ui.SubCategoryFragment.Companion.SUB_CATEGORY_TITLE
import com.d9tilov.android.category.ui.navigation.EditCategoryDialogNavigator
import com.d9tilov.android.category.ui.vm.CategoryGroupEditViewModel
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.category_ui.databinding.FragmentDialogEditCategoryBinding
import com.d9tilov.android.common.android.ui.base.BaseDialogFragment
import com.d9tilov.android.common.android.utils.onChange
import com.d9tilov.android.common.android.utils.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryEditDialog :
    BaseDialogFragment<EditCategoryDialogNavigator, FragmentDialogEditCategoryBinding>(FragmentDialogEditCategoryBinding::inflate),
    EditCategoryDialogNavigator {

    private val args by navArgs<CategoryEditDialogArgs>()
    private val category by lazy { args.category }
    override fun getNavigator() = this
    override val viewModel by viewModels<CategoryGroupEditViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            categoryDialogEditEtName.setText(category.name)
            categoryDialogEditEtName.onChange { text ->
                val isNameEmpty = text.isEmpty()
                categoryEditButtonConfirm.isEnabled = !isNameEmpty
                categoryDialogEditEtNameLayout.error = null
            }
            categoryEditButtonConfirm.setOnClickListener {
                viewModel.save(category.copy(name = categoryDialogEditEtName.text.toString()))
            }
            categoryEditButtonCancel.setOnClickListener { dismiss() }
        }
    }

    override fun onStart() {
        super.onStart()
        showKeyboard(viewBinding?.categoryDialogEditEtName)
    }

    override fun showError(error: Throwable) {
        if (error is CategoryException.CategoryExistException) {
            viewBinding?.categoryDialogEditEtNameLayout?.error =
                getString(R.string.category_unit_name_exist_error)
        }
    }

    override fun save() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            SUB_CATEGORY_TITLE,
            category.copy(name = viewBinding?.categoryDialogEditEtName?.text.toString())
        )
        dismiss()
    }

    override fun closeDialog() {
        dismiss()
    }
}
