package com.d9tilov.moneymanager.incomeexpense.income.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.callback.SwipeToDeleteCallback
import com.d9tilov.moneymanager.base.ui.navigator.IncomeNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.events.OnKeyboardVisibleChange
import com.d9tilov.moneymanager.core.ui.recyclerview.GridSpaceItemDecoration
import com.d9tilov.moneymanager.core.ui.recyclerview.ItemSnapHelper
import com.d9tilov.moneymanager.core.ui.recyclerview.StickyHeaderItemDecorator
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.core.util.isTablet
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.databinding.FragmentIncomeBinding
import com.d9tilov.moneymanager.home.ui.MainActivity
import com.d9tilov.moneymanager.incomeexpense.ui.BaseIncomeExpenseFragment
import com.d9tilov.moneymanager.incomeexpense.ui.IncomeExpenseFragmentDirections
import com.d9tilov.moneymanager.transaction.TransactionType
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

@AndroidEntryPoint
class IncomeFragment :
    BaseIncomeExpenseFragment<IncomeNavigator>(R.layout.fragment_income),
    IncomeNavigator,
    OnKeyboardVisibleChange {

    private val viewBinding by viewBinding(FragmentIncomeBinding::bind)

    override fun getNavigator() = this
    override val viewModel by viewModels<IncomeViewModel>()
    override val snackBarAnchorView by lazy { viewBinding.incomeCategoryRvList }

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.run {
            emptyViewStub = root.findViewById(R.id.income_transaction_empty_placeholder)
            incomeMainSum.moneyEditText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    incomeMainSum.moneyEditText.post {
                        incomeMainSum.moneyEditText.setSelection(incomeMainSum.moneyEditText.text.toString().length)
                    }
                }
            }
            mainSum = incomeMainSum
        }
        viewModel.run {
            getCategories().observe(
                viewLifecycleOwner,
                { list ->
                    val sortedCategories = list.sortedWith(
                        compareBy(
                            { it.children.isEmpty() },
                            { -it.usageCount },
                            { it.name }
                        )
                    )
                    categoryAdapter.updateItems(sortedCategories)
                }
            )
            getTransactions().observe(
                viewLifecycleOwner,
                {
                    lifecycleScope.launchWhenCreated {
                        transactionAdapter.submitData(it)
                        transactionAdapter.loadStateFlow.collectLatest { loadStates ->
                            isTransactionDataEmpty =
                                loadStates.source.refresh is LoadState.NotLoading &&
                                    loadStates.append.endOfPaginationReached &&
                                    transactionAdapter.itemCount == 0
                            if (isTransactionDataEmpty && !(activity as MainActivity).forceShowKeyboard) {
                                showViewStub(TransactionType.INCOME)
                            } else {
                                hideViewStub()
                            }
                        }
                    }
                }
            )
            getTransactionEvent().observe(
                viewLifecycleOwner,
                {
                    hideKeyboard()
                    resetMainSum()
                }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        if ((activity as MainActivity).forceShowKeyboard) {
            showKeyboard(viewBinding.incomeMainSum)
        } else {
            viewBinding.run {
                incomeTransactionRvList.show()
                incomeCategoryRvList.gone()
            }
        }
    }

    override fun initCategoryRecyclerView() {
        viewBinding.run {
            val layoutManager =
                GridLayoutManager(
                    requireContext(),
                    if (requireContext().isTablet()) TABLET_SPAN_COUNT else SPAN_COUNT,
                    GridLayoutManager.HORIZONTAL,
                    false
                )
            incomeCategoryRvList.layoutManager = layoutManager
            incomeCategoryRvList.adapter = categoryAdapter
            incomeCategoryRvList.addItemDecoration(
                GridSpaceItemDecoration(
                    SPAN_COUNT,
                    resources.getDimension(R.dimen.recycler_view_category_offset).toInt(),
                    layoutManager.orientation
                )
            )
            val snapHelper: SnapHelper =
                ItemSnapHelper()
            snapHelper.attachToRecyclerView(incomeCategoryRvList)
        }
    }

    override fun initTransactionsRecyclerView() {
        viewBinding.run {
            incomeTransactionRvList.layoutManager =
                LinearLayoutManager(requireContext())
            incomeTransactionRvList.adapter = transactionAdapter
            val itemDecoration =
                StickyHeaderItemDecorator(
                    transactionAdapter
                )
            itemDecoration.attachToRecyclerView(incomeTransactionRvList)
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    transactionAdapter.deleteItem(viewHolder.adapterPosition)
                }
            }).attachToRecyclerView(incomeTransactionRvList)
        }
    }

    override fun openCategoriesScreen() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param(FirebaseAnalytics.Param.ITEM_ID, "click_all_categories_income")
        }
        viewBinding.run {
            val inputSum = incomeMainSum.getValue()
            val action = if (inputSum.signum() > 0) {
                IncomeExpenseFragmentDirections.toCategoryDest(
                    destination = CategoryDestination.MAIN_WITH_SUM_SCREEN,
                    sum = inputSum,
                    transactionType = TransactionType.INCOME
                )
            } else {
                IncomeExpenseFragmentDirections.toCategoryDest(
                    destination = CategoryDestination.MAIN_SCREEN,
                    transactionType = TransactionType.INCOME
                )
            }
            findNavController().navigate(action)
        }
    }

    override fun onOpenKeyboard() {
        Timber.tag(App.TAG).d("Keyboard shown")
        isKeyboardOpen = true
        viewBinding.run {
            onKeyboardVisibilityAnimation(true)
            incomeTransactionRvList.gone()
            hideViewStub()
            incomeCategoryRvList.scrollToPosition(0)
        }
    }

    override fun onCloseKeyboard() {
        Timber.tag(App.TAG).d("Keyboard hidden")
        isKeyboardOpen = false
        viewBinding.run {
            onKeyboardVisibilityAnimation(false)
            if (isTransactionDataEmpty) {
                showViewStub(TransactionType.INCOME)
            }
            incomeMainSum.clearFocus()
        }
    }

    private fun onKeyboardVisibilityAnimation(open: Boolean) {
        if (open) {
            viewBinding.incomeCategoryRvList.show()
        } else {
            viewBinding.incomeTransactionRvList.show()
            viewBinding.incomeCategoryRvList.gone()
        }
        val alphaAnimationCategories =
            ObjectAnimator.ofFloat(
                viewBinding.incomeCategoryRvList,
                View.ALPHA,
                if (open) ALPHA_CAT_MIN else ALPHA_MAX,
                if (open) ALPHA_MAX else ALPHA_CAT_MIN
            ).apply {
                duration = ANIMATION_DURATION_CAT
                interpolator = AccelerateInterpolator()
            }
        val alphaAnimationTransactions =
            ObjectAnimator.ofFloat(
                viewBinding.incomeCategoryRvList,
                View.ALPHA,
                if (open) ALPHA_MAX else ALPHA_CAT_MIN,
                if (open) ALPHA_CAT_MIN else ALPHA_MAX
            ).apply {
                duration = ANIMATION_DURATION_TRANSACTION
                interpolator = AccelerateInterpolator()
            }
        alphaAnimationTransactions.start()
        alphaAnimationCategories.start()
    }

    override fun saveTransaction(category: Category) {
        viewModel.saveTransaction(category, viewBinding.incomeMainSum.getValue())
    }

    override fun resetMainSum() {
        viewBinding.incomeMainSum.setValue(BigDecimal.ZERO)
    }

    companion object {
        fun newInstance() = IncomeFragment()
    }
}
