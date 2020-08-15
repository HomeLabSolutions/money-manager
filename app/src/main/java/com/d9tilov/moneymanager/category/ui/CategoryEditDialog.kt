package com.d9tilov.moneymanager.category.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseDialogFragment
import com.d9tilov.moneymanager.base.ui.navigator.EditCategoryDialogNavigator
import com.d9tilov.moneymanager.category.exception.CategoryExistException
import com.d9tilov.moneymanager.category.subcategory.SubCategoryFragment.Companion.SUB_CATEGORY_TITLE
import com.d9tilov.moneymanager.category.ui.vm.CategoryGroupEditViewModel
import com.d9tilov.moneymanager.core.util.onChange
import com.d9tilov.moneymanager.databinding.FragmentDialogEditCategoryBinding
import com.mfms.common.util.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryEditDialog :
    BaseDialogFragment<FragmentDialogEditCategoryBinding, EditCategoryDialogNavigator>(),
    EditCategoryDialogNavigator {

    private val args by navArgs<CategoryEditDialogArgs>()
    private val category by lazy { args.category }

    override val layoutId = R.layout.fragment_dialog_edit_category
    override fun performDataBinding(view: View) = FragmentDialogEditCategoryBinding.bind(view)
    override fun getNavigator() = this
    override val viewModel by viewModels<CategoryGroupEditViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.let {
            it.categoryDialogEditEtName.setText(category.name)
            it.categoryDialogEditLimit.text = getString(
                R.string.letter_limit,
                it.categoryDialogEditEtName.text.length.toString(),
                resources.getInteger(R.integer.max_category_name_length).toString()
            )
            it.categoryDialogEditEtName.onChange { text ->
                val isNameEmpty = text.isEmpty()
                viewBinding?.categoryEditButtonConfirm?.isEnabled = !isNameEmpty
                viewBinding?.categoryDialogEditError?.visibility = View.INVISIBLE
                viewBinding?.categoryDialogEditLimit?.text = getString(
                    R.string.letter_limit,
                    text.length.toString(),
                    resources.getInteger(R.integer.max_category_name_length).toString()
                )
            }
            it.categoryEditButtonConfirm.setOnClickListener { _ ->
                viewModel.save(category.copy(name = it.categoryDialogEditEtName.text.toString()))
            }
            it.categoryEditButtonCancel.setOnClickListener { dismiss() }
        }
    }

    override fun onStart() {
        super.onStart()
        showKeyboard(viewBinding?.categoryDialogEditEtName)
    }

    override fun showError(error: Throwable) {
        if (error is CategoryExistException) {
            viewBinding?.categoryDialogEditError?.text =
                getString(R.string.category_unit_name_exist_error)
        }
        viewBinding?.categoryDialogEditError?.visibility = View.VISIBLE
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
