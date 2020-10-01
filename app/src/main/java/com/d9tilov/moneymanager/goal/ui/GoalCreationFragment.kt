package com.d9tilov.moneymanager.goal.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.CreatedGoalNavigator
import com.d9tilov.moneymanager.core.util.onChange
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.databinding.FragmentCreationGoalBinding
import com.d9tilov.moneymanager.goal.vm.CreatedGoalViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoalCreationFragment :
    BaseFragment<FragmentCreationGoalBinding, CreatedGoalNavigator>(R.layout.fragment_creation_goal),
    CreatedGoalNavigator {

    private val args by navArgs<GoalCreationFragmentArgs>()
    private val goal by lazy { args.goal }

    private var toolbar: MaterialToolbar? = null

    override fun performDataBinding(view: View) = FragmentCreationGoalBinding.bind(view)

    override fun getNavigator() = this

    override val viewModel by viewModels<CreatedGoalViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        goal?.run {
            viewBinding?.run {
                createdGoalName.setText(name)
                createdGoalSum.setValue(targetSum)
                createdGoalDescription.setText(description)
            }
        }
        viewBinding?.run {
            createdGoalName.onChange { updateSaveButtonState() }
            createdGoalSum.moneyEditText.onChange { updateSaveButtonState() }
            createdGoalSave.setOnClickListener {
                if (it.isSelected) {
                    viewModel.save(
                        createdGoalName.text.toString(),
                        createdGoalSum.getValue(),
                        createdGoalDescription.text.toString()
                    )
                } else {
                    shakeError()
                }
            }
        }
    }

    private fun shakeError() {
        val animation = AnimationUtils.loadAnimation(
            context,
            R.anim.animation_shake
        )
        viewBinding?.run {
            if (createdGoalName.text?.isEmpty() == true) {
                createdGoalNameLayout.startAnimation(animation)
            } else if (createdGoalSum.getValue().signum() == 0) {
                createdGoalSum.startAnimation(animation)
            }
        }
    }

    private fun initToolbar() {
        toolbar = viewBinding?.createdGoalToolbarContainer?.toolbar
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.title_goal)
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        viewBinding?.run { showKeyboard(createdGoalName) }
    }

    private fun updateSaveButtonState() {
        viewBinding?.run {
            createdGoalSave.isSelected =
                createdGoalSum.getValue().signum() > 0 &&
                    createdGoalName.text?.isNotEmpty() == true
        }
    }

    override fun back() {
        findNavController().popBackStack()
    }
}
