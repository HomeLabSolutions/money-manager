package com.d9tilov.moneymanager.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme
import com.d9tilov.moneymanager.prepopulate.PrepopulateScreen
import com.d9tilov.moneymanager.ui.MmApp
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val providers = arrayListOf(
        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )
    private val startForResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
            Timber.tag(DataConstants.TAG).d("Sign in result: $result")
            viewModel.updateData()
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    uiState = state
                    when (uiState) {
                        is MainActivityUiState.Success.Auth -> {
                            viewModel.setToLoadingState()
                            startForResult.launch(
                                AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setLogo(com.d9tilov.android.designsystem.R.drawable.ic_money_manager_logo)
                                    .setTheme(com.d9tilov.android.designsystem.R.style.Theme_Login)
                                    .setAvailableProviders(providers)
                                    .setIsSmartLockEnabled(false)
                                    .build()
                            )
                        }

                        is MainActivityUiState.Success.Main -> runMain()
                        is MainActivityUiState.Success.Prepopulate -> setContent { MoneyManagerTheme { PrepopulateScreen() } }
                        else -> {}
                    }
                }
            }
        }

        // Keep the splash screen on-screen until the UI state is loaded. This condition is
        // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
        // the UI.
        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                is MainActivityUiState.Loading -> true
                is MainActivityUiState.Success -> false
            }
        }

    }

    private fun runMain() {
        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations, and go edge-to-edge
        // This also sets up the initial system bar style based on the platform theme
        setContent {
            val darkTheme = isSystemInDarkTheme()

            // Update the edge to edge configuration to match the theme
            // This is the same parameters as the default enableEdgeToEdge call, but we manually
            // resolve whether or not to show dark theme using uiState, since it can be different
            // than the configuration's dark theme value based on the user preference.
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT,
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ) { darkTheme },
                )
                onDispose {}
            }

            MoneyManagerTheme(darkTheme = darkTheme) {
                MmApp(windowSizeClass = calculateWindowSizeClass(this))
            }
        }
    }
}

@Suppress("MagicNumber")
private val lightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
@Suppress("MagicNumber")
private val darkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
