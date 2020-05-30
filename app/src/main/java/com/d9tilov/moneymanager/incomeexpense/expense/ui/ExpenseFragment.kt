package com.d9tilov.moneymanager.incomeexpense.expense.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SnapHelper
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.base.ui.recyclerview.ItemSnapHelper
import com.d9tilov.moneymanager.base.ui.recyclerview.SpaceItemDecoration
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.ui.CategoryAdapter
import com.d9tilov.moneymanager.core.ui.widget.currencyview.PinButton
import com.d9tilov.moneymanager.core.ui.widget.currencyview.PinKeyboard
import com.d9tilov.moneymanager.core.util.events.OnItemClickListener
import com.d9tilov.moneymanager.databinding.FragmentExpenseBinding
import kotlinx.android.synthetic.main.keyboard_layout.view.pin_keyboard
import timber.log.Timber

class ExpenseFragment : BaseFragment<FragmentExpenseBinding, ExpenseNavigator, ExpenseViewModel>(), ExpenseNavigator {

    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter = CategoryAdapter()
        categoryAdapter.itemClickListener = onItemClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            transactionMainContent.pin_keyboard.setClickPinButton(PinClickListener())
            expenseCategoryRvList.layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
            expenseCategoryRvList.adapter = categoryAdapter
            expenseCategoryRvList.addItemDecoration(
                SpaceItemDecoration(
                    requireContext(),
                    R.dimen.recycler_view_category_offset
                )
            )
            val snapHelper: SnapHelper = ItemSnapHelper()
            snapHelper.attachToRecyclerView(expenseCategoryRvList)
        }
        viewModel.categories.observe(
            this.viewLifecycleOwner,
            Observer { categoryAdapter.updateItems(it) })
    }

    inner class PinClickListener : PinKeyboard.ClickPinButton {
        override fun onPinClick(pinButton: PinButton) {
            when (val child = pinButton.getChildAt(0)) {
                is TextView -> {
                    val digit = child.text.toString()
                    viewModel.updateNum(viewBinding.expenseMainSum.getSum())
                    viewBinding.expenseMainSum.setSum(viewModel.tapNum(digit))
                }
                is ImageView -> {
                    viewModel.updateNum(viewBinding.expenseMainSum.getSum())
                    viewBinding.expenseMainSum.setSum(viewModel.removeNum())
                }
            }
        }
    }

    private val onItemClickListener = object : OnItemClickListener<Category> {
        override fun onItemClick(item: Category, position: Int) {
            viewModel.onCategoryClicked(item)
        }
    }

    override fun performDataBinding(view: View): FragmentExpenseBinding =
        FragmentExpenseBinding.bind(view)

    override fun getLayoutId() = R.layout.fragment_expense
    override fun getViewModelClass() = ExpenseViewModel::class.java
    override fun getNavigator() = this
    override fun openCategoriesScreen() {
        Timber.tag("moggot").d("activity = %s", activity)
        findNavController().navigate(R.id.action_mainFragment_to_category_dest)
    }
}
