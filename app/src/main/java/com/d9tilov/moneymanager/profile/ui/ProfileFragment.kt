package com.d9tilov.moneymanager.profile.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d9tilov.moneymanager.BuildConfig
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.backup.PeriodicBackupWorker
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.ProfileNavigator
import com.d9tilov.moneymanager.budget.BudgetDestination
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
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
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<ProfileNavigator>(R.layout.fragment_profile),
    ProfileNavigator {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val viewBinding by viewBinding(FragmentProfileBinding::bind)

    override fun getNavigator() = this
    override val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
        viewBinding.run {
            profileLogout.setOnClickListener {
                AuthUI.getInstance()
                    .signOut(requireContext())
                    .addOnCompleteListener {
                        viewModel.logout()
                        PeriodicBackupWorker.stopPeriodicJob(requireContext())
                        updateUI()
                    }
            }
            profileCurrency.profileCurrencyLayout.setOnClickListener {
                val action =
                    ProfileFragmentDirections.toCurrencyDest(CurrencyDestination.PROFILE_SCREEN)
                findNavController().navigate(action)
            }
            profileBudget.profileBudgetLayout.setOnClickListener {
                val action =
                    ProfileFragmentDirections.toBudgetDest(BudgetDestination.PROFILE_SCREEN)
                findNavController().navigate(action)
            }
            profileRegularIncomes.profileRegularIncomesLayout.setOnClickListener {
                val action = ProfileFragmentDirections.toRegularIncomesDest(
                    RegularTransactionDestination.PROFILE_SCREEN
                )
                findNavController().navigate(action)
            }
            profileRegularExpenses.profileRegularExpensesLayout.setOnClickListener {
                val action =
                    ProfileFragmentDirections.toRegularExpensesDest(RegularTransactionDestination.PROFILE_SCREEN)
                findNavController().navigate(action)
            }
            profileGoals.profileGoalsLayout.setOnClickListener {
                val action = ProfileFragmentDirections.toGoalsDest(GoalDestination.PROFILE_SCREEN)
                findNavController().navigate(action)
            }
            profileSettings.profileSettingsLayout.setOnClickListener {
                val action = ProfileFragmentDirections.toSettingsDest()
                findNavController().navigate(action)
            }
            profileSettings.profileSettingsLayout.setOnClickListener {
                val action = ProfileFragmentDirections.toSettingsDest()
                findNavController().navigate(action)
            }
            viewModel.userData().observe(
                viewLifecycleOwner,
                {
                    val currencyCode = it.currencyCode
                    val icon = CurrencyUtils.getCurrencyIcon(currencyCode)
                    val currencyTitle = CurrencyUtils.getCurrencySignBy(currencyCode)
                    viewBinding.profileCurrency.profileItemCurrencyIcon.text = icon
                    viewBinding.profileCurrency.profileItemCurrencySign.text = currencyTitle
                }
            )
            viewModel.budget().observe(
                viewLifecycleOwner,
                {
                    viewBinding.profileBudget.profileItemBudgetValue.setValue(it.sum)
                    viewBinding.profileBudget.profileItemBudgetCreatedDate.text =
                        getString(
                            R.string.budget_date_created,
                            it.createdDate.toBudgetCreatedDate()
                        )
                }
            )
            viewModel.regularIncomes().observe(
                viewLifecycleOwner,
                { list ->
                    val incomes = list.joinToString(separator = ",") { it.category.name }
                    when (incomes.isEmpty()) {
                        true ->
                            viewBinding.profileRegularIncomes.profileItemRegularIncomeTitle.text =
                                getString(R.string.profile_item_regular_incomes_title_empty)
                        false ->
                            viewBinding.profileRegularIncomes.profileItemRegularIncomeTitle.text =
                                getString(R.string.profile_item_regular_incomes_title, incomes)
                    }
                }
            )
            viewModel.regularExpenses().observe(
                viewLifecycleOwner,
                { list ->
                    val expenses = list.joinToString(separator = ",") { it.category.name }
                    when (expenses.isEmpty()) {
                        true ->
                            viewBinding.profileRegularExpenses.profileItemRegularExpenseTitle.text =
                                getString(R.string.profile_item_regular_expenses_title_empty)
                        false ->
                            viewBinding.profileRegularExpenses.profileItemRegularExpenseTitle.text =
                                getString(R.string.profile_item_regular_expenses_title, expenses)
                    }
                }
            )
            viewModel.goals().observe(
                viewLifecycleOwner,
                { list ->
                    val goals = list.joinToString(separator = ",") { it.name }
                    when (goals.isEmpty()) {
                        true ->
                            viewBinding.profileGoals.profileItemGoalTitle.text =
                                getString(R.string.profile_item_goals_title_empty)
                        false ->
                            viewBinding.profileGoals.profileItemGoalTitle.text =
                                getString(R.string.profile_item_goals_title, goals)
                    }
                }
            )
        }
    }

    private fun updateUI() {
        viewBinding.profileAppVersion.text = BuildConfig.VERSION_NAME
        val currencyUser = viewModel.getCurrentUser()
        if (currencyUser == null) {
            startActivity(
                Intent(context, SplashActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
            requireActivity().finish()
        } else {
            viewBinding.run {
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
}
