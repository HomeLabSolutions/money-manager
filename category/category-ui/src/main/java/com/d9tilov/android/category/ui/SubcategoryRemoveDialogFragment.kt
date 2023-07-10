package com.d9tilov.android.category.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.android.category.ui.navigation.RemoveSubCategoryDialogNavigator
import com.d9tilov.android.category.ui.vm.SubcategoryRemoveViewModel
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.designsystem.databinding.FragmentDialogTripleRemoveBinding
import com.d9tilov.android.common_android.ui.base.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubcategoryRemoveDialogFragment :
    BaseDialogFragment<RemoveSubCategoryDialogNavigator, FragmentDialogTripleRemoveBinding>(
        FragmentDialogTripleRemoveBinding::inflate
    ), RemoveSubCategoryDialogNavigator {

    private val args by navArgs<SubcategoryRemoveDialogFragmentArgs>()
    private val subCategory by lazy { args.subcategory }

    override fun getNavigator() = this

    override val viewModel by viewModels<SubcategoryRemoveViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            tripleRemoveDialogButtonAction1.text = getString(R.string.sub_category_delete)
            tripleRemoveDialogButtonAction2.text =
                getString(R.string.sub_category_delete_from_group)
            tripleRemoveDialogButtonCancel.text = getString(com.d9tilov.android.common_android.R.string.cancel)
            tripleRemoveDialogTitle.text = getString(R.string.sub_category_delete_title)
            tripleRemoveDialogButtonAction1.setOnClickListener { viewModel.remove(subCategory) }
            tripleRemoveDialogButtonAction2.setOnClickListener { viewModel.removeFromGroup(subCategory) }
            tripleRemoveDialogButtonCancel.setOnClickListener { dismiss() }
        }
    }

    override fun closeDialog() {
        dismiss()
        findNavController().popBackStack(R.id.category_creation_dest, true)
    }

    override fun closeDialogAndGoToCategory() {
        dismiss()
        findNavController().popBackStack(R.id.sub_category_dest, true)
    }
}
