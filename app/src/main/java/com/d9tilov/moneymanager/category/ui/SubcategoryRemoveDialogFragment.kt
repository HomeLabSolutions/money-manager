package com.d9tilov.moneymanager.category.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseDialogFragment
import com.d9tilov.moneymanager.base.ui.navigator.RemoveSubCategoryDialogNavigator
import com.d9tilov.moneymanager.category.ui.vm.SubcategoryRemoveViewModel
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.databinding.FragmentDialogTripleRemoveBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubcategoryRemoveDialogFragment :
    BaseDialogFragment<RemoveSubCategoryDialogNavigator>(),
    RemoveSubCategoryDialogNavigator {

    private val args by navArgs<SubcategoryRemoveDialogFragmentArgs>()
    private val subCategory by lazy { args.subcategory }
    private val viewBinding by viewBinding(FragmentDialogTripleRemoveBinding::bind)

    override val layoutId = R.layout.fragment_dialog_triple_remove
    override fun getNavigator() = this

    override val viewModel by viewModels<SubcategoryRemoveViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.run {
            tripleRemoveDialogButtonAction1.text = getString(R.string.sub_category_delete)
            tripleRemoveDialogButtonAction2.text =
                getString(R.string.sub_category_delete_from_group)
            tripleRemoveDialogButtonCancel.text = getString(R.string.cancel)
            tripleRemoveDialogTitle.text = getString(R.string.sub_category_delete_title)
            tripleRemoveDialogSubtitle.text =
                getString(R.string.sub_category_delete_subtitle, subCategory.name)
            tripleRemoveDialogButtonAction1.setOnClickListener { viewModel.remove(subCategory) }
            tripleRemoveDialogButtonAction2.setOnClickListener {
                viewModel.removeFromGroup(
                    subCategory
                )
            }
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
