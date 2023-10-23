package com.d9tilov.android.category.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.android.category.ui.navigation.CategoryDestination
import com.d9tilov.android.category.ui.navigation.RemoveCategoryDialogNavigator
import com.d9tilov.android.category.ui.vm.CategoryRemoveViewModel
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.common.android.ui.base.BaseDialogFragment
import com.d9tilov.android.designsystem.databinding.FragmentDialogRemoveBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryRemoveDialog :
    BaseDialogFragment<RemoveCategoryDialogNavigator,
        FragmentDialogRemoveBinding>(FragmentDialogRemoveBinding::inflate),
    RemoveCategoryDialogNavigator {

    private val args by navArgs<CategoryRemoveDialogArgs>()
    private val destination by lazy { args.destination }

    override fun getNavigator() = this
    override val viewModel by viewModels<CategoryRemoveViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            removeDialogTitle.text = getString(R.string.category_delete_title)
            removeDialogSubtitle.text =
                getString(
                    if (viewModel.category.value.children.isEmpty()) {
                        R.string.category_delete_sub_title
                    } else {
                        R.string.category_delete_group_sub_title
                    }
                )
            removeButtonConfirm.setOnClickListener { viewModel.remove() }
            removeButtonCancel.setOnClickListener { dismiss() }
        }
    }

    override fun closeDialog() {
        dismiss()
        if (destination == CategoryDestination.SubCategoryScreen) {
            findNavController().popBackStack(R.id.sub_category_dest, true)
        } else {
            findNavController().popBackStack(R.id.category_creation_dest, true)
        }
    }
}