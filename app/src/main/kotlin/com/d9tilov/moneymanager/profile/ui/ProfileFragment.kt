package com.d9tilov.moneymanager.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.ProfileNavigator
import com.d9tilov.android.budget.ui.navigation.budgetScreen
import com.d9tilov.android.budget.ui.navigation.navigateToBudgetScreen
import com.d9tilov.android.ui.navigation.currencyScreen
import com.d9tilov.android.ui.navigation.navigateToCurrencyListScreen
import com.d9tilov.moneymanager.databinding.FragmentProfileBinding
import com.d9tilov.moneymanager.profile.navigation.profileNavigationRoute
import com.d9tilov.moneymanager.profile.navigation.profileScreen
import com.d9tilov.moneymanager.profile.ui.vm.ProfileViewModel
import com.d9tilov.moneymanager.settings.navigation.navigateToSettingsScreen
import com.d9tilov.moneymanager.settings.navigation.settingsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<ProfileNavigator, FragmentProfileBinding>(
        FragmentProfileBinding::inflate,
        R.layout.fragment_profile
    ),
    ProfileNavigator {

    override fun getNavigator() = this
    override val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = profileNavigationRoute
                ) {
                    profileScreen(
                        navigateToCurrencyListScreen = navController::navigateToCurrencyListScreen,
                        navigateToBudgetScreen = navController::navigateToBudgetScreen,
                        navigateToSettingsScreen = navController::navigateToSettingsScreen,
                        navigateToGoalsScreen = navController::navigateToSettingsScreen
                    )
                    currencyScreen { navController.popBackStack() }
                    budgetScreen { navController.popBackStack() }
                    settingsScreen { navController.popBackStack() }
                }
            }
        }
    }
}

