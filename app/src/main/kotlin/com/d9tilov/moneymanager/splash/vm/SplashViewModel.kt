package com.d9tilov.moneymanager.splash.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.backup.domain.BackupInteractor
import com.d9tilov.moneymanager.base.data.local.exceptions.NetworkException
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.base.ui.navigator.SplashNavigator
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.encryption.equalsDigest
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.FileNotFoundException
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesStore: PreferencesStore,
    private val backupInteractor: BackupInteractor,
    private val userInteractor: Lazy<UserInteractor>,
    private val categoryInteractor: Lazy<CategoryInteractor>
) : BaseViewModel<SplashNavigator>() {

    private val auth = FirebaseAuth.getInstance()

    fun onStartup() {
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
                        if (userInteractor.get().showPrepopulate()) {
                            withContext(Dispatchers.Main) { navigator?.openPrepopulate() }
                        } else withContext(Dispatchers.Main) { navigator?.openHomeScreen() }
                    }
                }
            }
        }
    }

    fun updateData() {
        viewModelScope.launch(Dispatchers.IO) {
            auth.currentUser?.let { firebaseUser ->
                preferencesStore.updateUid(firebaseUser.uid) // need for dataBase decryption
                try {
                    backupInteractor.restoreBackup()
                } catch (ex: NetworkException) {
                    Timber.tag(App.TAG).d("Do work with network exception: $ex")
                } catch (ex: WrongUidException) {
                    Timber.tag(App.TAG).d("Do work with wrong uid exception: $ex")
                } catch (ex: FileNotFoundException) {
                    Timber.tag(App.TAG).d("Do work with file not found error: $ex")
                } catch (ex: FirebaseException) {
                    Timber.tag(App.TAG).d("Do work with exception: $ex")
                }
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
