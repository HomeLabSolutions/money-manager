package com.d9tilov.moneymanager.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.d9tilov.moneymanager.R
import com.firebase.ui.auth.FirebaseAuthUI
import com.firebase.ui.auth.configuration.authUIConfiguration
import com.firebase.ui.auth.configuration.auth_provider.AuthProvider
import com.firebase.ui.auth.configuration.theme.AuthUITheme
import com.firebase.ui.auth.ui.method_picker.AuthMethodPicker
import com.firebase.ui.auth.ui.screens.FirebaseAuthScreen
import kotlinx.coroutines.launch
import timber.log.Timber

private const val LOGO_SIZE = 88
private const val LOGO_CORNER_RADIUS = 20
private const val LOGO_WEIGHT = 0.4f
private const val LOGO_FOREGROUND_SCALE = 1.5f

@Composable
internal fun SignInScreen(onSignInSuccess: () -> Unit) {
    val context = LocalContext.current.applicationContext
    val authTheme = AuthUITheme.fromMaterialTheme()
    val configuration =
        remember(context, authTheme) {
            authUIConfiguration {
                this.context = context
                providers {
                    provider(
                        AuthProvider.Email(
                            emailLinkActionCodeSettings = null,
                            passwordValidationRules = emptyList(),
                        ),
                    )
                    provider(
                        AuthProvider.Phone(
                            defaultNumber = null,
                            defaultCountryCode = null,
                            allowedCountries = null,
                        ),
                    )
                    provider(AuthProvider.Google(scopes = listOf("email", "profile"), serverClientId = null))
                }
                theme = authTheme
            }
        }

    val authUI = remember { FirebaseAuthUI.getInstance() }
    val onGoogleSignIn = rememberGoogleSignIn(authUI, onSignInSuccess)

    FirebaseAuthScreen(
        authUI = authUI,
        configuration = configuration,
        onSignInSuccess = { onSignInSuccess() },
        onSignInFailure = { Timber.e(it, "Sign in failed") },
        onSignInCancelled = {},
        customMethodPickerLayout = { providers, onProviderSelected ->
            SignInMethodPicker(providers = providers) { provider ->
                if (provider is AuthProvider.Google) {
                    onGoogleSignIn(provider)
                } else {
                    onProviderSelected(provider)
                }
            }
        },
    )
}

@Composable
private fun rememberGoogleSignIn(
    authUI: FirebaseAuthUI,
    onSignInSuccess: () -> Unit,
): (AuthProvider.Google) -> Unit {
    val context = LocalContext.current
    val googleSignIn = remember(context, authUI) { GoogleSignIn(context, authUI) }
    val scope = rememberCoroutineScope()
    return { provider ->
        scope.launch {
            if (googleSignIn.signIn(requireNotNull(provider.serverClientId))) {
                onSignInSuccess()
            }
        }
    }
}

@Composable
private fun SignInMethodPicker(
    providers: List<AuthProvider>,
    onProviderSelected: (AuthProvider) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
        Box(
            modifier = Modifier.fillMaxWidth().weight(LOGO_WEIGHT),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier.size(LOGO_SIZE.dp).clip(RoundedCornerShape(LOGO_CORNER_RADIUS.dp)),
                contentAlignment = Alignment.Center,
            ) {
                // Crop the adaptive icon's transparent inset before rounding its visible edge.
                Image(
                    painter = painterResource(R.mipmap.ic_launcher_foreground),
                    contentDescription = stringResource(R.string.sign_in_logo),
                    modifier = Modifier.requiredSize((LOGO_SIZE * LOGO_FOREGROUND_SCALE).dp),
                )
            }
        }
        AuthMethodPicker(
            modifier = Modifier.fillMaxWidth().weight(1f),
            providers = providers,
            onProviderSelected = onProviderSelected,
        )
    }
}
