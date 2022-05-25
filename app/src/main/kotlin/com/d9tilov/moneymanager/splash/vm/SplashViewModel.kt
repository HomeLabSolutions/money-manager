package com.d9tilov.moneymanager.splash.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.backup.domain.BackupInteractor
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.base.ui.navigator.SplashNavigator
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.encryption.equalsDigest
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.google.firebase.auth.FirebaseAuth
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect
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
        viewModelScope.launch(Dispatchers.IO) {
            val firebaseUid = auth.currentUser?.uid
            if (firebaseUid == null) {
                withContext(Dispatchers.Main) { navigator?.openAuthScreen() }
            } else {
                preferencesStore.uid.collect { uid ->
                    if (!auth.uid.equalsDigest(uid)) {
                        if (uid != null) userInteractor.get().deleteUser()
                        auth.signOut()
                        withContext(Dispatchers.Main) { navigator?.openAuthScreen() }
                    } else {
                        if (userInteractor.get().showPrepopulate())
                            withContext(Dispatchers.Main) { navigator?.openPrepopulate() }
                        else withContext(Dispatchers.Main) { navigator?.openHomeScreen() }
                    }
                }
            }
        }
    }

    fun updateData() {
        viewModelScope.launch(Dispatchers.IO) {
            auth.currentUser?.let { firebaseUser ->
                preferencesStore.updateUid(firebaseUser.uid) // need for dataBase decryption
                backupInteractor.restoreBackup()
                val user = userInteractor.get().getCurrentUser().firstOrNull()
                if (user == null || user.showPrepopulate) {
                    userInteractor.get().createUser(auth.currentUser)
                    categoryInteractor.get().createDefaultCategories()
                    withContext(Dispatchers.Main) { navigator?.openPrepopulate() }
                } else {
                    preferencesStore.updateCurrentCurrency(user.currentCurrencyCode)
                    withContext(Dispatchers.Main) { navigator?.openHomeScreen() }
                }
            }
        }
    }
}
