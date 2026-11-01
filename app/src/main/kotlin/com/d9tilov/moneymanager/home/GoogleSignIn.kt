package com.d9tilov.moneymanager.home

import android.content.Context
import android.content.MutableContextWrapper
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.d9tilov.moneymanager.R
import com.firebase.ui.auth.AuthException
import com.firebase.ui.auth.AuthState
import com.firebase.ui.auth.FirebaseAuthUI
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthMultiFactorException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

internal class GoogleSignIn(
    private val context: Context,
    private val authUI: FirebaseAuthUI,
) {
    suspend fun signIn(serverClientId: String): Boolean {
        authUI.updateAuthState(AuthState.Loading(context.getString(R.string.sign_in_google_loading)))
        try {
            val token = requestIdToken(serverClientId)
            val credential = GoogleAuthProvider.getCredential(token, null)
            val result = authUI.auth.signInWithCredential(credential).await()
            if (result.user == null) throw invalidCredential()
            authUI.updateAuthState(AuthState.Idle)
            return true
        } catch (_: GetCredentialCancellationException) {
            authUI.updateAuthState(AuthState.Idle)
        } catch (exception: CancellationException) {
            authUI.updateAuthState(AuthState.Idle)
            throw exception
        } catch (exception: NoCredentialException) {
            reportFailure(exception)
        } catch (exception: GetCredentialException) {
            reportFailure(exception)
        } catch (exception: GoogleIdTokenParsingException) {
            reportFailure(exception)
        } catch (exception: FirebaseAuthMultiFactorException) {
            authUI.updateAuthState(AuthState.RequiresMfa(resolver = exception.resolver))
        } catch (exception: FirebaseException) {
            reportFailure(exception)
        } catch (exception: AuthException) {
            reportFailure(exception)
        }
        return false
    }

    private suspend fun requestIdToken(serverClientId: String): String {
        // The button flow also lets users add an account when none is on the device.
        val option = GetSignInWithGoogleOption.Builder(serverClientId).build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
        val result =
            CredentialManager.create(context).getCredential(
                context = MutableContextWrapper(context),
                request = request,
            )
        val credential = result.credential
        if (
            credential !is CustomCredential ||
            credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            throw invalidCredential()
        }
        return GoogleIdTokenCredential.createFrom(credential.data).idToken
    }

    private fun invalidCredential(): AuthException =
        AuthException.InvalidCredentialsException(
            message = context.getString(R.string.sign_in_google_error),
        )

    private fun reportFailure(exception: Exception) {
        authUI.updateAuthState(AuthState.Error(AuthException.from(exception, context)))
    }
}
