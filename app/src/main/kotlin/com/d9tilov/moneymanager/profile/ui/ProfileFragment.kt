package com.d9tilov.moneymanager.profile.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.d9tilov.moneymanager.BuildConfig
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.currencyCode
import com.d9tilov.moneymanager.base.ui.navigator.ProfileNavigator
import com.d9tilov.moneymanager.budget.domain.entity.BudgetDestination
import com.d9tilov.moneymanager.core.util.CurrencyUtils
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.core.util.toBudgetCreatedDate
import com.d9tilov.moneymanager.currency.domain.entity.CurrencyDestination
import com.d9tilov.moneymanager.databinding.FragmentProfileBinding
import com.d9tilov.moneymanager.goal.domain.entity.GoalDestination
import com.d9tilov.moneymanager.profile.ui.vm.ProfileViewModel
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransactionDestination
import com.d9tilov.moneymanager.splash.ui.RouterActivity
import dagger.hilt.android.AndroidEntryPoint
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
            profileCurrency.root.setOnClickListener { openCurrencyScreen() }
            profileBudget.root.setOnClickListener { openBudgetScreen() }
            profileRegularIncomes.root.setOnClickListener { openRegularIncomesScreen() }
            profileRegularExpenses.root.setOnClickListener { openRegularExepenseScreen() }
            profileGoals.root.setOnClickListener { openGoalScreen() }
            profileSettings.root.setOnClickListener { openSettingsScreen() }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.userData.collect { profile ->
                            profile?.currentCurrencyCode?.let { code ->
                                val icon = CurrencyUtils.getCurrencyIcon(code)
                                val currencyTitle = CurrencyUtils.getCurrencySignBy(code)
                                profileCurrency.profileItemCurrentCurrencyIcon.text = icon
                                profileCurrency.profileItemCurrentCurrencySign.text = currencyTitle
                            }
                        }
                    }
                    launch {
                        viewModel.budget.collect { budget ->
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
                        viewModel.regularIncomes.collect { list ->
                            val incomes = list.joinToString(separator = ", ") { it.category.name }
                            profileRegularIncomes.profileItemRegularIncomeSubtitle.text = incomes
                        }
                    }
                    launch {
                        viewModel.regularExpenses.collect { list ->
                            val expenses = list.joinToString(separator = ", ") { it.category.name }
                            profileRegularExpenses.profileItemRegularExpenseSubtitle.text = expenses
                        }
                    }
                    launch {
                        viewModel.isPremium.collect { isPremium ->
                            if (isPremium) {
                                profileGoals.root.show()
                                profileRegularExpenses.root.show()
                                profileRegularIncomes.root.show()
                                profileSettings.profileSettingsPremium.text =
                                    getString(R.string.settings_subscription_premium_acknowledged_title)
                                profileSettings.profileSettingsPremium.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_round_corner_positive)
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) profileSettings.profileSettingsPremium.setTextAppearance(
                                    requireContext(),
                                    R.style.Widget_MoneyManager_TextView_Label_Positive
                                )
                                else profileSettings.profileSettingsPremium.setTextAppearance(R.style.Widget_MoneyManager_TextView_Label_Positive)
                            } else {
                                profileSettings.profileSettingsPremium.text =
                                    getString(R.string.settings_subscription_premium_title)
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                    profileSettings.profileSettingsPremium.setTextAppearance(
                                        requireContext(),
                                        R.style.Widget_MoneyManager_TextView_Label_Neutral
                                    )
                                } else profileSettings.profileSettingsPremium.setTextAppearance(R.style.Widget_MoneyManager_TextView_Label_Neutral)
                                profileSettings.profileSettingsPremium.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_round_corner_neutral)
                            }
                            profileSettings.profileSettingsPremium.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.billing_premium_label_text_size))
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
        val action = ProfileFragmentDirections.toGoalsDest(GoalDestination.ProfileScreen)
        findNavController().navigate(action)
    }

    private fun openRegularExepenseScreen() {
        val action =
            ProfileFragmentDirections.toRegularExpensesDest(RegularTransactionDestination.ProfileScreen)
        findNavController().navigate(action)
    }

    private fun openRegularIncomesScreen() {
        val action =
            ProfileFragmentDirections.toRegularIncomesDest(RegularTransactionDestination.ProfileScreen)
        findNavController().navigate(action)
    }

    private fun openBudgetScreen() {
        val action = ProfileFragmentDirections.toBudgetDest(BudgetDestination.ProfileScreen)
        findNavController().navigate(action)
    }

    private fun openCurrencyScreen() {
        val action =
            ProfileFragmentDirections.toCurrencyDest(CurrencyDestination.ProfileCurrentScreen)
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
                Glide
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
            Intent(context, RouterActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        requireActivity().finish()
    }
}
