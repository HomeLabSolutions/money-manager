package com.d9tilov.moneymanager.category.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.CategoryCreationNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.exception.CategoryExistException
import com.d9tilov.moneymanager.category.ui.CategoryIconSetFragment.Companion.ARG_CATEGORY_ICON_ID
import com.d9tilov.moneymanager.category.ui.recycler.CategoryColorAdapter
import com.d9tilov.moneymanager.category.ui.vm.CategoryCreationViewModel
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_DATA_ID
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.hide
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.core.util.onChange
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.databinding.FragmentCreationCategoryBinding
import com.d9tilov.moneymanager.transaction.TransactionType
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryCreationFragment :
    BaseFragment<CategoryCreationNavigator>(
        R.layout.fragment_creation_category
    ),
    CategoryCreationNavigator {

    private val args by navArgs<CategoryCreationFragmentArgs>()
    private val transactionType by lazy { args.transactionType }
    private val category by lazy {
        args.category ?: Category(
            type = transactionType,
            name = "",
            icon = if (transactionType == TransactionType.EXPENSE) R.drawable.ic_category_expense_food else R.drawable.ic_category_income_business,
            color = R.color.category_pink
        )
    }
    private val viewBinding by viewBinding(FragmentCreationCategoryBinding::bind)

    @ColorRes
    private var color: Int = 0

    @DrawableRes
    private var icon: Int = 0

    private var toolbar: MaterialToolbar? = null
    private lateinit var categoryColorAdapter: CategoryColorAdapter

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun getNavigator() = this
    override val viewModel by viewModels<CategoryCreationViewModel> {
        CategoryCreationViewModel.provideFactory(
            categoryCreationViewModelFactory,
            this,
            arguments
        )
    }

    @Inject
    lateinit var categoryCreationViewModelFactory: CategoryCreationViewModel.CategoryCreationViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryColorAdapter = CategoryColorAdapter(category.color)
        categoryColorAdapter.itemClickListener = onItemColorClickListener
    }

    private val onItemColorClickListener = object : OnItemClickListener<Int> {
        override fun onItemClick(item: Int, position: Int) {
            color = item
            viewBinding.run {
                categoryCreationRvColorPicker.hide()
                updateIcon(icon)
                setColorToColorIcon()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<@DrawableRes Int>(
            ARG_CATEGORY_ICON_ID
        )?.observe(
            viewLifecycleOwner
        ) {
            updateIcon(it)
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<@DrawableRes Int>(
                ARG_CATEGORY_ICON_ID
            )
        }
        color = category.color
        viewBinding.run {
            val colorLayoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            categoryCreationRvColorPicker.layoutManager = colorLayoutManager
            categoryCreationRvColorPicker.adapter = categoryColorAdapter
            categoryCreationRvColorPicker.scrollToPosition(categoryColorAdapter.getSelectedPosition())
            categoryCreationEtName.setText(category.name)
            updateIcon(category.icon)
            setColorToColorIcon()
            categoryCreationSave.isEnabled =
                categoryCreationEtName.length() > 0
            categoryCreationEtName.onChange { text ->
                categoryCreationSave.isEnabled = text.isNotEmpty()
                categoryCreationEtNameLayout.error = null
            }
            if (category.id == DEFAULT_DATA_ID) {
                categoryCreationDelete.gone()
            } else {
                categoryCreationDelete.show()
            }
            categoryCreationIconLayout.setOnClickListener {
                val action = CategoryCreationFragmentDirections.toCategorySetDest(transactionType)
                findNavController().navigate(action)
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                    param(FirebaseAnalytics.Param.ITEM_ID, "open_category_set_screen")
                }
            }
            categoryCreationSave.setOnClickListener {
                viewModel.save(
                    category.copy(
                        name = categoryCreationEtName.text.toString(),
                        color = color,
                        icon = icon
                    )
                )
            }
            categoryCreationColor.root.setOnClickListener {
                categoryCreationRvColorPicker.show()
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                    param(FirebaseAnalytics.Param.ITEM_ID, "click_choose_color")
                }
            }
            categoryCreationDelete.setOnClickListener {
                if (category.id == DEFAULT_DATA_ID) {
                    return@setOnClickListener
                }
                val action: NavDirections = if (category.parent != null) {
                    CategoryCreationFragmentDirections.toRemoveSubCategoryDialog(category)
                } else {
                    CategoryCreationFragmentDirections.toRemoveCategoryDialog(
                        CategoryDestination.CATEGORY_CREATION_SCREEN,
                        category
                    )
                }
                findNavController().navigate(action)
            }
        }
        initToolbar()
    }

    override fun onStart() {
        if (category.id == DEFAULT_DATA_ID) {
            showKeyboard(viewBinding.categoryCreationEtName)
        }
        super.onStart()
    }

    override fun onStop() {
        hideKeyboard()
        super.onStop()
    }

    private fun setColorToColorIcon() {
        viewBinding.categoryCreationColor.colorCircle.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), color))
    }

    private fun updateIcon(@DrawableRes iconId: Int) {
        this.icon = iconId
        val tintIcon = createTintDrawable(
            requireContext(),
            icon,
            color
        )
        viewBinding.categoryCreationIcon.setImageDrawable(tintIcon)
    }

    private fun initToolbar() {
        toolbar = viewBinding.categoryCreationToolbarContainer?.toolbar
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.title =
            getString(if (category.id != DEFAULT_DATA_ID) R.string.title_category_creation else R.string.title_category_creation_create)
    }

    override fun save() {
        findNavController().popBackStack()
    }

    override fun showError(error: Throwable) {
        if (error is CategoryExistException) {
            viewBinding.categoryCreationEtNameLayout.error =
                getString(R.string.category_unit_name_exist_error)
        }
    }
}
