package com.d9tilov.moneymanager.goal.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.FragmentGoalsBinding
import com.d9tilov.moneymanager.databinding.LayoutEmptyListGoalsPlaceholderBinding
import com.d9tilov.moneymanager.goal.GoalDestination
import com.d9tilov.moneymanager.goal.domain.entity.Goal
import com.d9tilov.moneymanager.goal.ui.dialog.GoalRemoveDialog.Companion.ARG_UNDO_REMOVE_LAYOUT_DISMISS
import com.d9tilov.moneymanager.goal.vm.GoalsViewModel
import com.d9tilov.moneymanager.home.ui.currencyCode
import com.d9tilov.moneymanager.prepopulate.ui.ControlsClicked
import com.d9tilov.moneymanager.prepopulate.ui.PrepopulateActivity
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GoalsFragment :
    BaseFragment<GoalsNavigator, FragmentGoalsBinding>(
        FragmentGoalsBinding::inflate,
        R.layout.fragment_goals
    ),
    GoalsNavigator,
    ControlsClicked {

    private val args by navArgs<GoalsFragmentArgs>()
    private val destination by lazy { args.destination }

    private var toolbar: MaterialToolbar? = null
    private var emptyViewStub: LayoutEmptyListGoalsPlaceholderBinding? = null
    private val goalAdapter: GoalAdapter = GoalAdapter()
    private var showViewStub = false

    override fun getNavigator() = this
    override val viewModel by viewModels<GoalsViewModel>()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goalAdapter.itemClickListener = onItemClickListener
        goalAdapter.itemSwipeListener = onItemSwipeListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewBinding?.run {
            emptyViewStub = goalsEmptyPlaceholder
            if (destination != GoalDestination.PREPOPULATE_SCREEN) {
                goalsSave.show()
            }
            goalsRvList.adapter = goalAdapter
            val layoutManager = LinearLayoutManager(requireContext())
            goalsRvList.layoutManager = layoutManager
            goalsRvList.addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.recycler_view_regular_category_margin).toInt()
                )
            )
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    goalAdapter.deleteItem(viewHolder.bindingAdapterPosition)
                }
            }).attachToRecyclerView(goalsRvList)
            goalsSumPerPeriod.moneyEditText.clearFocus()
            goalsSave.setOnClickListener { viewModel.insertSavedSum(goalsSumPerPeriod.getValue()) }

            findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
                ARG_UNDO_REMOVE_LAYOUT_DISMISS
            )?.observe(viewLifecycleOwner) { if (it) goalAdapter.cancelDeletion() }
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch { viewModel.accumulated.collect { goalsAmount.setValue(it, currencyCode()) } }
                    launch { viewModel.budget.collect { goalsSumPerPeriod.setValue(it.saveSum, currencyCode()) } }
                    launch {
                        viewModel.goals.collect {
                            if (it.isEmpty()) {
                                goalsRvList.gone()
                                showViewStub()
                            } else {
                                hideViewStub()
                                goalsRvList.show()
                                goalAdapter.updateItems(it)
                            }
                            showViewStub = it.isEmpty()
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (activity is PrepopulateActivity) {
            (activity as PrepopulateActivity).controlsClick = this
        }
        hideKeyboard()
    }

    override fun onStop() {
        super.onStop()
        if (activity is PrepopulateActivity) {
            (activity as PrepopulateActivity).controlsClick = null
        }
    }

    private fun initToolbar() {
        toolbar = viewBinding?.goalsToolbarContainer?.toolbar
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.title_goal)
        setHasOptionsMenu(true)
        if (destination == GoalDestination.PROFILE_SCREEN) {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_add -> openCreationGoalScreen()
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
        emptyViewStub?.let {
            it.root.show()
            it.emptyPlaceholderIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_goal_placeholder
                )
            )
            it.emptyPlaceholderTitle.text = getString(R.string.goals_empty_placeholder_title)
            val stubSubTitle = it.emptyPlaceholderSubtitle
            stubSubTitle.show()
            stubSubTitle.text = getString(R.string.goals_empty_placeholder_subtitle)
            val addButton = it.emptyPlaceholderAdd
            addButton.show()
            addButton.setOnClickListener { openCreationGoalScreen() }
        }
    }

    private fun hideViewStub() {
        emptyViewStub?.root?.gone()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.prepopulate_menu, menu)
        menu.findItem(R.id.action_skip).isVisible = false
        menu.findItem(R.id.action_add).isVisible = true
    }

    override fun onNextClick() {
        viewBinding?.goalsSumPerPeriod?.let { viewModel.savePrepopulateStatusAndSavedSum(it.getValue()) }
    }

    override fun save() {
        findNavController().popBackStack()
    }
}
