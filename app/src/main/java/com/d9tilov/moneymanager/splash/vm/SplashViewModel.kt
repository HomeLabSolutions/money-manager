package com.d9tilov.moneymanager.splash.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.base.ui.navigator.SplashNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val preferencesStore: PreferencesStore) :
    BaseViewModel<SplashNavigator>() {

    private val auth = FirebaseAuth.getInstance()

    override fun onNavigatorAttached() {
        super.onNavigatorAttached()
        if (auth.currentUser == null) navigator?.openAuthScreen()
        else {
            viewModelScope.launch {
                combine(
                    preferencesStore.uid,
                    preferencesStore.prefillCompleted
                ) { uid, prefillCompleted ->
                    if (uid != auth.uid) {
                        auth.signOut()
                        navigator?.openAuthScreen()
                    } else {
                        if (prefillCompleted) navigator?.openHomeScreen()
                        else navigator?.openPrepopulate()
                    }
                }
            }
        }
    }

    fun updateUid() {
        viewModelScope.launch(Dispatchers.IO) {
            auth.currentUser?.let { preferencesStore.updateUid(it.uid) }
        }
    }
}
