package com.d9tilov.moneymanager.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.d9tilov.moneymanager.R
import com.firebase.ui.auth.configuration.authUIConfiguration
import com.firebase.ui.auth.configuration.auth_provider.AuthProvider
import com.firebase.ui.auth.configuration.theme.AuthUIAsset
import com.firebase.ui.auth.configuration.theme.AuthUITheme
import com.firebase.ui.auth.ui.screens.FirebaseAuthScreen
import timber.log.Timber

@Composable
internal fun SignInScreen(
    onSignInSuccess: () -> Unit,
    onSignInCancelled: () -> Unit,
) {
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
                logo = AuthUIAsset.Resource(R.mipmap.ic_launcher_foreground)
                theme = authTheme
            }
        }

    FirebaseAuthScreen(
        configuration = configuration,
        onSignInSuccess = { onSignInSuccess() },
        onSignInFailure = { Timber.e(it, "Sign in failed") },
        onSignInCancelled = onSignInCancelled,
    )
}
