package com.d9tilov.moneymanager.splash.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.network.exception.NetworkException
import com.d9tilov.android.user.domain.contract.UserInteractor
import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.moneymanager.base.ui.navigator.SplashNavigator
import com.d9tilov.android.category.data.contract.CategoryInteractor
import com.d9tilov.android.common_android.ui.base.BaseViewModel
import com.d9tilov.android.user.ui.mapper.toDataModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.FileNotFoundException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltViewModel
class RouterViewModel @Inject constructor(
    private val preferencesStore: PreferencesStore,
    private val backupInteractor: com.d9tilov.android.backup.domain.contract.BackupInteractor,
    private val userInteractor: UserInteractor,
    private val categoryInteractor: Lazy<com.d9tilov.android.category.data.contract.CategoryInteractor>,
) : BaseViewModel<SplashNavigator>() {

    private val auth = FirebaseAuth.getInstance()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesStore.uid.collect { uid ->
                val firebaseUid = auth.currentUser?.uid
                if (firebaseUid == null) {
                    withContext(Dispatchers.Main) { navigator?.openAuthScreen() }
                } else {
                    if (firebaseUid != uid) {
                        if (uid != null) userInteractor.deleteUser()
                        auth.signOut()
                        withContext(Dispatchers.Main) { navigator?.openAuthScreen() }
                    } else {
                        var user = userInteractor.getCurrentUser().firstOrNull()
                        if (user == null) {
                            try {
                                backupInteractor.restoreBackup()
                            } catch (ex: NetworkException) {
                                Timber.tag(TAG).d("Do work with network exception: $ex")
                            } catch (ex: WrongUidException) {
                                Timber.tag(TAG).d("Do work with wrong uid exception: $ex")
                            } catch (ex: FileNotFoundException) {
                                Timber.tag(TAG).d("Do work with file not found error: $ex")
                            } catch (ex: FirebaseException) {
                                Timber.tag(TAG).d("Do work with exception: $ex")
                            }
                            user = userInteractor.getCurrentUser().firstOrNull()
                            if (user == null || user.showPrepopulate) {
                                userInteractor.createUser(auth.currentUser.toDataModel())
                                categoryInteractor.get().createDefaultCategories()
                                withContext(Dispatchers.Main) { navigator?.openPrepopulate() }
                            } else {
                                withContext(Dispatchers.Main) { navigator?.openHomeScreen() }
                            }
                        } else {
                            if (userInteractor.showPrepopulate())
                                withContext(Dispatchers.Main) { navigator?.openPrepopulate() }
                            else withContext(Dispatchers.Main) { navigator?.openHomeScreen() }
                        }
                    }
                }
            }
        }
    }

    fun updateUid() {
        viewModelScope.launch(Dispatchers.IO) {
            auth.currentUser?.let { firebaseUser -> preferencesStore.updateUid(firebaseUser.uid) }
        }
    }
}
