package com.d9tilov.moneymanager.goal.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.callback.SwipeToDeleteCallback
import com.d9tilov.moneymanager.base.ui.navigator.GoalsNavigator
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.events.OnItemSwipeListener
import com.d9tilov.moneymanager.core.ui.recyclerview.MarginItemDecoration
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.FragmentGoalsBinding
import com.d9tilov.moneymanager.goal.domain.entity.Goal
import com.d9tilov.moneymanager.goal.ui.dialog.GoalRemoveDialog.Companion.ARG_UNDO_REMOVE_LAYOUT_DISMISS
import com.d9tilov.moneymanager.goal.vm.GoalsViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoalsFragment :
    BaseFragment<GoalsNavigator>(R.layout.fragment_goals),
    GoalsNavigator {

    private var toolbar: MaterialToolbar? = null
    private var emptyViewStub: ViewStub? = null
    private val goalAdapter: GoalAdapter = GoalAdapter()
    private val viewBinding by viewBinding(FragmentGoalsBinding::bind)

    private val onItemClickListener = object : OnItemClickListener<Goal> {
        override fun onItemClick(item: Goal, position: Int) {
            val action = GoalsFragmentDirections.toGoalsCreationDest(item)
            findNavController().navigate(action)
        }
    }
    private val onItemSwipeListener = object : OnItemSwipeListener<Goal> {
        override fun onItemSwiped(item: Goal, position: Int) {
            openRemoveConfirmationDialog(item)
        }
    }

    private fun openRemoveConfirmationDialog(goal: Goal) {
        val action = GoalsFragmentDirections.toRemoveGoalDialog(goal)
        findNavController().navigate(action)
    }
    override fun getNavigator() = this

    override val viewModel by viewModels<GoalsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goalAdapter.itemClickListener = onItemClickListener
        goalAdapter.itemSwipeListener = onItemSwipeListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        emptyViewStub = viewBinding.root.findViewById(R.id.goals_empty_placeholder)
        viewBinding.run {
            goalsRvList.adapter = goalAdapter
            val layoutManager = LinearLayoutManager(requireContext())
            goalsRvList.layoutManager = layoutManager
            goalsRvList.addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.recycler_view_fixed_category_margin).toInt()
                )
            )
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    goalAdapter.deleteItem(viewHolder.adapterPosition)
                }
            }).attachToRecyclerView(goalsRvList)
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            ARG_UNDO_REMOVE_LAYOUT_DISMISS
        )?.observe(
            viewLifecycleOwner
        ) {
            if (it) {
                goalAdapter.cancelDeletion()
            }
        }
        viewModel.amount.observe(
            this.viewLifecycleOwner,
            { viewBinding.goalsAmount.setValue(it) }
        )
        toolbar = viewBinding.goalsToolbarContainer.toolbar
        viewModel.goals.observe(
            this.viewLifecycleOwner,
            {
                if (it.isEmpty()) {
                    viewBinding.goalsRvList.gone()
                    showViewStub()
                } else {
                    hideViewStub()
                    viewBinding.goalsRvList.show()
                    goalAdapter.updateItems(it)
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()
        hideKeyboard()
    }

    private fun initToolbar() {
        toolbar = viewBinding.goalsToolbarContainer.toolbar
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.title_goal)
        activity.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_add -> {
                    openCreationGoalScreen()
                }
                else -> throw IllegalArgumentException("Unknown menu item with id: ${it.itemId}")
            }
            return@setOnMenuItemClickListener false
        }
    }

    private fun openCreationGoalScreen(goal: Goal? = null) {
        val action = GoalsFragmentDirections.toGoalsCreationDest(goal)
        findNavController().navigate(action)
    }

    private fun showViewStub() {
        if (emptyViewStub?.parent == null) {
            emptyViewStub?.visibility = View.VISIBLE
        } else {
            val inflatedStub = emptyViewStub?.inflate()
            val stubIcon =
                inflatedStub?.findViewById<ImageView>(R.id.empty_placeholder_icon)
            stubIcon?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_goal
                )
            )
            val stubTitle =
                inflatedStub?.findViewById<TextView>(R.id.empty_placeholder_title)
            stubTitle?.text = getString(R.string.goals_empty_placeholder_title)
            val stubSubTitle =
                inflatedStub?.findViewById<TextView>(R.id.empty_placeholder_subtitle)
            stubSubTitle?.visibility = View.VISIBLE
            stubSubTitle?.text = getString(R.string.goals_empty_placeholder_subtitle)
            val addButton =
                inflatedStub?.findViewById<ImageView>(R.id.empty_placeholder_add)
            addButton?.visibility = View.VISIBLE
            addButton?.setOnClickListener {
                openCreationGoalScreen()
            }
        }
    }

    private fun hideViewStub() {
        emptyViewStub?.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.prepopulate_menu, menu)
        menu.findItem(R.id.action_skip).isVisible = false
        menu.findItem(R.id.action_add).isVisible = true
    }
}
