package com.d9tilov.android.category.ui

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.d9tilov.android.category.ui.navigation.CategoryDestination
import com.d9tilov.android.category.ui.recycler.CategoryModifyAdapter
import com.d9tilov.android.category.ui.vm.BaseCategoryViewModel
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.category_ui.databinding.FragmentCategoryBinding
import com.d9tilov.android.common.android.ui.base.BaseFragment
import com.d9tilov.android.common.android.ui.base.BaseNavigator
import com.d9tilov.android.common.android.ui.recyclerview.GridSpaceItemDecoration
import com.d9tilov.android.core.events.OnBackPressed
import com.d9tilov.android.core.model.TransactionType
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

abstract class BaseCategoryFragment<N : BaseNavigator> :
    BaseFragment<N, FragmentCategoryBinding>(
        FragmentCategoryBinding::inflate,
        R.layout.fragment_category
    ),
    OnBackPressed {

    private val args by navArgs<CategoryFragmentArgs>()
    protected val destination: CategoryDestination by lazy { args.destination }
    protected val transactionType: TransactionType by lazy { args.transactionType }
    protected var toolbar: MaterialToolbar? = null
    protected val categoryAdapter: CategoryModifyAdapter =
        CategoryModifyAdapter(
            { item, _ -> (viewModel as BaseCategoryViewModel<N>).onCategoryClicked(item) },
            { item, _ -> (viewModel as BaseCategoryViewModel<N>).onCategoryRemoved(item) }
        )

    override fun onStop() {
        categoryAdapter.enableEditMode(false)
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewBinding?.run {
            val layoutManager =
                GridLayoutManager(requireContext(), SPAN_COUNT, GridLayoutManager.VERTICAL, false)
            categoryRv.layoutManager = layoutManager
            categoryRv.adapter = categoryAdapter
            categoryRv.addItemDecoration(
                GridSpaceItemDecoration(
                    spanCount = SPAN_COUNT,
                    spacing = resources.getDimension(com.d9tilov.android.common.android.R.dimen.recycler_view_category_offset)
                        .toInt()
                )
            )
            categoryRv.addOnItemTouchListener(object : OnItemTouchListener {
                override fun onInterceptTouchEvent(
                    recyclerView: RecyclerView,
                    motionEvent: MotionEvent
                ): Boolean {
                    if (motionEvent.action != MotionEvent.ACTION_UP) {
                        return false
                    }
                    val child =
                        recyclerView.findChildViewUnder(motionEvent.x, motionEvent.y)
                    if (child == null) {
                        categoryAdapter.enableEditMode(false)
                        return true
                    }
                    return false
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) { /* do nothing */
                }

                override fun onTouchEvent(
                    recyclerView: RecyclerView,
                    motionEvent: MotionEvent
                ) { /* do nothing */
                }
            })
        }
        viewLifecycleOwner.lifecycleScope.launch {
            (viewModel as BaseCategoryViewModel<*>).categories
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { list ->
                    val sortedList = list.sortedWith(
                        compareBy(
                            { it.children.isEmpty() },
                            { -it.usageCount },
                            { it.name }
                        )
                    )
                    categoryAdapter.updateItems(sortedList)
                }
        }
    }

    private fun initToolbar() {
        toolbar = viewBinding?.categoryToolbarContainer?.toolbar
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onBackPressed(): Boolean {
        return if (categoryAdapter.editModeEnable) {
            categoryAdapter.enableEditMode(false)
            false
        } else {
            true
        }
    }

    companion object {
        const val SPAN_COUNT = 4
        const val ARG_CATEGORY = "arg_category"
    }
}
