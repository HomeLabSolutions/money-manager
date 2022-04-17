package com.d9tilov.moneymanager.profile.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.d9tilov.moneymanager.BuildConfig
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.currencyCode
import com.d9tilov.moneymanager.base.ui.navigator.ProfileNavigator
import com.d9tilov.moneymanager.budget.BudgetDestination
import com.d9tilov.moneymanager.core.util.CurrencyUtils
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.core.util.toBudgetCreatedDate
import com.d9tilov.moneymanager.currency.CurrencyDestination
import com.d9tilov.moneymanager.databinding.FragmentProfileBinding
import com.d9tilov.moneymanager.goal.GoalDestination
import com.d9tilov.moneymanager.profile.ui.vm.ProfileViewModel
import com.d9tilov.moneymanager.regular.RegularTransactionDestination
import com.d9tilov.moneymanager.splash.ui.SplashActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<ProfileNavigator, FragmentProfileBinding>(FragmentProfileBinding::inflate, R.layout.fragment_profile),
    ProfileNavigator {

    override fun getNavigator() = this
    override val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
        viewBinding?.run {
            profileLogout.setOnClickListener { logout() }
            profileCurrency.profileCurrencyLayout.setOnClickListener { openCurrencyScreen() }
            profileBudget.profileBudgetLayout.setOnClickListener { openBudgetScreen() }
            profileRegularIncomes.profileRegularIncomesLayout.setOnClickListener { openRegularIncomesScreen() }
            profileRegularExpenses.profileRegularExpensesLayout.setOnClickListener { openRegularExepenseScreen() }
            profileGoals.profileGoalsTitle.setOnClickListener { openGoalScreen() }
            profileSettings.profileSettingsTitle.setOnClickListener { openSettingsScreen() }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.userData().collect { profile ->
                            profile?.currentCurrencyCode?.let { code ->
                                val icon = CurrencyUtils.getCurrencyIcon(code)
                                val currencyTitle = CurrencyUtils.getCurrencySignBy(code)
                                profileCurrency.profileItemCurrentCurrencyIcon.text = icon
                                profileCurrency.profileItemCurrentCurrencySign.text = currencyTitle
                            }
                        }
                    }
                    launch {
                        viewModel.budget().collect { budget ->
                            profileBudget.profileItemBudgetValue.setValue(
                                budget.sum,
                                currencyCode()
                            )
                            profileBudget.profileItemBudgetCreatedDate.text =
                                getString(
                                    R.string.budget_date_created,
                                    budget.createdDate.toBudgetCreatedDate()
                                )
                        }
                    }
                    launch {
                        viewModel.regularIncomes().collect { list ->
                            val incomes = list.joinToString(separator = ", ") { it.category.name }
                            profileRegularIncomes.profileItemRegularIncomeSubtitle.text = incomes
                        }
                    }
                    launch {
                        viewModel.regularExpenses().collect { list ->
                            val expenses = list.joinToString(separator = ", ") { it.category.name }
                            profileRegularExpenses.profileItemRegularExpenseSubtitle.text = expenses
                        }
                    }
                    launch {
                        viewModel.goals().collect { list ->
                            val goals = list.joinToString(separator = ",") { it.name }
                            when (goals.isEmpty()) {
                                true ->
                                    profileGoals.profileGoalsTitle.text =
                                        getString(R.string.profile_item_goals_title_empty)
                                false ->
                                    profileGoals.profileGoalsTitle.text =
                                        getString(R.string.profile_item_goals_title, goals)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun openSettingsScreen() {
        val action = ProfileFragmentDirections.toSettingsDest()
        findNavController().navigate(action)
    }

    private fun openGoalScreen() {
        val action = ProfileFragmentDirections.toGoalsDest(GoalDestination.PROFILE_SCREEN)
        findNavController().navigate(action)
    }

    private fun openRegularExepenseScreen() {
        val action =
            ProfileFragmentDirections.toRegularExpensesDest(RegularTransactionDestination.PROFILE_SCREEN)
        findNavController().navigate(action)
    }

    private fun openRegularIncomesScreen() {
        val action =
            ProfileFragmentDirections.toRegularIncomesDest(RegularTransactionDestination.PROFILE_SCREEN)
        findNavController().navigate(action)
    }

    private fun openBudgetScreen() {
        val action = ProfileFragmentDirections.toBudgetDest(BudgetDestination.PROFILE_SCREEN)
        findNavController().navigate(action)
    }

    private fun openCurrencyScreen() {
        val action =
            ProfileFragmentDirections.toCurrencyDest(CurrencyDestination.PROFILE_SCREEN_CURRENT)
        findNavController().navigate(action)
    }

    private fun logout() {
        val action = ProfileFragmentDirections.toLogoutDialogDest()
        findNavController().navigate(action)
    }

    private fun updateUI() {
        viewBinding?.run {
            profileAppVersion.text = BuildConfig.VERSION_NAME
            val currencyUser = viewModel.getCurrentUser()
            if (currencyUser == null) restart()
            else {
                this.profileAvatar.show()
                profileName.show()
                profileName.text = currencyUser.displayName
                GlideApp
                    .with(this@ProfileFragment)
                    .load(viewModel.getCurrentUser()?.photoUrl)
                    .centerCrop()
                    .into(profileAvatar)
                profileLogout.show()
            }
        }
    }

    private fun restart() {
        startActivity(
            Intent(context, SplashActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        requireActivity().finish()
    }
}
