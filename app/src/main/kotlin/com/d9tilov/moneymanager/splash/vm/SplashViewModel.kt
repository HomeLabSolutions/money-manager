package com.d9tilov.moneymanager.splash.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.backup.domain.BackupInteractor
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.base.ui.navigator.SplashNavigator
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.google.firebase.auth.FirebaseAuth
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesStore: PreferencesStore,
    private val backupInteractor: BackupInteractor,
    private val userInteractor: Lazy<UserInteractor>,
    private val categoryInteractor: Lazy<CategoryInteractor>
) : BaseViewModel<SplashNavigator>() {

    private val auth = FirebaseAuth.getInstance()

    override fun onNavigatorAttached() {
        super.onNavigatorAttached()
        if (auth.currentUser == null) navigator?.openAuthScreen()
        else {
            viewModelScope.launch {
                preferencesStore.uid.collect { uid ->
                    if (uid != auth.uid) {
                        auth.signOut()
                        navigator?.openAuthScreen()
                    } else {
                        if (preferencesStore.showPrepopulate.first()) navigator?.openHomeScreen()
                        else navigator?.openPrepopulate()
                    }
                }
            }
        }
    }

    fun updateUid() {
        viewModelScope.launch(Dispatchers.IO) {
            auth.currentUser?.let { firebaseUser ->
                preferencesStore.updateUid(firebaseUser.uid) // need for decryption DataBase
                backupInteractor.restoreBackup()
                val user = userInteractor.get().getCurrentUser().firstOrNull()
                if (user == null || user.showPrepopulate) {
                    userInteractor.get().createUser(auth.currentUser)
                    categoryInteractor.get().createDefaultCategories()
                    withContext(Dispatchers.Main) { navigator?.openPrepopulate() }
                } else withContext(Dispatchers.Main) { navigator?.openHomeScreen() }
            }
        }
    }
}