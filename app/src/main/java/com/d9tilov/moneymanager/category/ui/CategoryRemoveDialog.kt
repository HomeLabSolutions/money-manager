package com.d9tilov.moneymanager.category.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseDialogFragment
import com.d9tilov.moneymanager.base.ui.navigator.RemoveCategoryDialogNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.ui.vm.CategoryRemoveViewModel
import com.d9tilov.moneymanager.databinding.FragmentDialogRemoveBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryRemoveDialog :
    BaseDialogFragment<RemoveCategoryDialogNavigator, FragmentDialogRemoveBinding>(FragmentDialogRemoveBinding::inflate),
    RemoveCategoryDialogNavigator {

    private val args by navArgs<CategoryRemoveDialogArgs>()
    private val category by lazy { args.category }
    private val destination by lazy { args.destination }

    override val layoutId = R.layout.fragment_dialog_remove
    override fun getNavigator() = this
    override val viewModel by viewModels<CategoryRemoveViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            removeDialogTitle.text = getString(R.string.category_delete_title)
            removeDialogSubtitle.text =
                getString(
                    if (category.children.isEmpty()) R.string.category_delete_sub_title else R.string.category_delete_group_sub_title,
                    category.name
                )
            removeButtonConfirm.setOnClickListener { viewModel.remove(category) }
            removeButtonCancel.setOnClickListener { dismiss() }
        }
    }

    override fun closeDialog() {
        dismiss()
        if (destination == CategoryDestination.SUB_CATEGORY_SCREEN) {
            findNavController().popBackStack(R.id.sub_category_dest, true)
        } else {
            findNavController().popBackStack(R.id.category_creation_dest, true)
        }
    }
}
