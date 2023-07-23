package com.d9tilov.android.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.d9tilov.android.common.android.ui.base.BaseFragment
import com.d9tilov.android.profile.ui.navigation.ProfileNavigator
import com.d9tilov.android.profile.ui.navigation.budgetScreen
import com.d9tilov.android.profile.ui.navigation.currencyScreen
import com.d9tilov.android.profile.ui.navigation.navigateToBudgetScreen
import com.d9tilov.android.profile.ui.navigation.navigateToCurrencyListScreen
import com.d9tilov.android.profile.ui.navigation.navigateToSettingsScreen
import com.d9tilov.android.profile.ui.navigation.profileNavigationRoute
import com.d9tilov.android.profile.ui.navigation.profileScreen
import com.d9tilov.android.profile.ui.navigation.settingsScreen
import com.d9tilov.android.profile.ui.vm.ProfileViewModel
import com.d9tilov.android.profile_ui.R
import com.d9tilov.android.profile_ui.databinding.FragmentProfileBinding
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
