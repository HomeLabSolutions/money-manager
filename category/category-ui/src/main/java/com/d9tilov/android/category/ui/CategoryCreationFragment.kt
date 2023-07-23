package com.d9tilov.android.category.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.domain.model.exception.CategoryException
import com.d9tilov.android.category.domain.model.isEmpty
import com.d9tilov.android.category.ui.CategoryIconSetFragment.Companion.ARG_CATEGORY_ICON_ID
import com.d9tilov.android.category.ui.navigation.CategoryCreationNavigator
import com.d9tilov.android.category.ui.navigation.CategoryDestination
import com.d9tilov.android.category.ui.recycler.CategoryColorAdapter
import com.d9tilov.android.category.ui.vm.CategoryCreationViewModel
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.category_ui.databinding.FragmentCreationCategoryBinding
import com.d9tilov.android.common.android.ui.base.BaseFragment
import com.d9tilov.android.common.android.utils.createTintDrawable
import com.d9tilov.android.common.android.utils.gone
import com.d9tilov.android.common.android.utils.let2
import com.d9tilov.android.common.android.utils.onChange
import com.d9tilov.android.common.android.utils.show
import com.d9tilov.android.common.android.utils.showKeyboard
import com.d9tilov.android.core.constants.DataConstants.DEFAULT_DATA_ID
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryCreationFragment :
    BaseFragment<CategoryCreationNavigator, FragmentCreationCategoryBinding>
    (FragmentCreationCategoryBinding::inflate, R.layout.fragment_creation_category),
    CategoryCreationNavigator {

    private val args by navArgs<CategoryCreationFragmentArgs>()
    private val category by lazy { args.category }

    private var toolbar: MaterialToolbar? = null
    private var localCategory: Category = Category.EMPTY_EXPENSE
    private val categoryColorAdapter by lazy {
        CategoryColorAdapter(category.color) { item, _ ->
            viewBinding?.run {
                categoryCreationRvColorPicker.gone()
                updateColor(item)
            }
        }
    } // must be lazy for properly initialization with args

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun getNavigator() = this
    override val viewModel by viewModels<CategoryCreationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<@DrawableRes Int>(
            ARG_CATEGORY_ICON_ID
        )
            ?.observe(viewLifecycleOwner) {
                updateIcon(it)
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<@DrawableRes Int>(
                    ARG_CATEGORY_ICON_ID
                )
            }
        viewBinding?.run {
            if (localCategory.isEmpty()) localCategory = category
            val colorLayoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            categoryCreationRvColorPicker.layoutManager = colorLayoutManager
            categoryCreationRvColorPicker.adapter = categoryColorAdapter
            categoryCreationRvColorPicker.scrollToPosition(categoryColorAdapter.getSelectedPosition())
            categoryCreationEtName.setText(localCategory.name)
            updateColor(localCategory.color)
            updateIcon(localCategory.icon)
            categoryCreationSave.isEnabled = categoryCreationEtName.length() > 0
            categoryCreationEtName.onChange { text ->
                categoryCreationSave.isEnabled = text.isNotEmpty()
                categoryCreationEtNameLayout.error = null
                localCategory = localCategory.copy(name = text)
            }
            if (localCategory.id == DEFAULT_DATA_ID) {
                categoryCreationDelete.gone()
            } else {
                categoryCreationDelete.show()
            }
            categoryCreationIconLayout.setOnClickListener {
                val action = if (viewModel.isPremium) {
                    CategoryCreationFragmentDirections.toGroupedCategorySetDest()
                } else {
                    CategoryCreationFragmentDirections.toCategorySetDest(com.d9tilov.android.category.domain.model.CategoryGroup.UNKNOWN)
                }
                findNavController().navigate(action)
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                    param(FirebaseAnalytics.Param.ITEM_ID, "open_category_set_screen")
                }
            }
            categoryCreationSave.setOnClickListener { viewModel.save(localCategory) }
            categoryCreationColor.root.setOnClickListener {
                categoryCreationRvColorPicker.show()
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                    param(FirebaseAnalytics.Param.ITEM_ID, "click_choose_color")
                }
            }
            categoryCreationDelete.setOnClickListener {
                if (localCategory.id == DEFAULT_DATA_ID) {
                    return@setOnClickListener
                }
                val action: NavDirections = if (localCategory.parent != null) {
                    CategoryCreationFragmentDirections.toRemoveSubCategoryDialog(localCategory)
                } else {
                    CategoryCreationFragmentDirections.toRemoveCategoryDialog(
                        CategoryDestination.CategoryCreationScreen,
                        localCategory
                    )
                }
                findNavController().navigate(action)
            }
        }
        initToolbar()
    }

    override fun onStart() {
        if (localCategory.id == DEFAULT_DATA_ID) showKeyboard(viewBinding?.categoryCreationEtName)
        super.onStart()
    }

    private fun updateColor(@ColorRes color: Int?) {
        if (color == null) return
        localCategory = localCategory.copy(color = color)
        invalidateItemCircleColor()
        invalidateIcon()
    }

    private fun invalidateItemCircleColor() {
        viewBinding?.categoryCreationColor?.colorCircle?.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), localCategory.color ?: com.d9tilov.android.category_data_impl.R.color.category_all_color))
    }

    private fun updateIcon(@DrawableRes iconId: Int?) {
        if (iconId == null) return
        localCategory = localCategory.copy(icon = iconId)
        invalidateIcon()
    }

    private fun invalidateIcon() {
        val tintIcon = let2(localCategory.icon, localCategory.color) { icon, color ->
            createTintDrawable(requireContext(), icon, color)
        }
        viewBinding?.categoryCreationIcon?.setImageDrawable(tintIcon)
    }

    private fun initToolbar() {
        toolbar = viewBinding?.categoryCreationToolbarContainer?.toolbar
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.title =
            getString(if (localCategory.id != DEFAULT_DATA_ID) R.string.title_category_creation else R.string.title_category_creation_create)
    }

    override fun save() {
        findNavController().popBackStack()
    }

    override fun showError(error: Throwable) {
        if (error is CategoryException.CategoryExistException) {
            viewBinding?.categoryCreationEtNameLayout?.error =
                getString(R.string.category_unit_name_exist_error)
        }
    }
}
