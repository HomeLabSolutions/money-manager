package com.d9tilov.moneymanager.home

import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.android.common.android.location.LocationProvider
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.contract.GeocodingInteractor
import com.d9tilov.android.currency.observer.contract.CurrencyUpdateObserver
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.user.domain.contract.UserInteractor
import com.google.firebase.auth.FirebaseAuth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelInitializationTest {
    private val dispatcher = StandardTestDispatcher()
    private val preferencesStore: PreferencesStore = mockk()
    private val firebaseAuth: FirebaseAuth = mockk()
    private val backupInteractor = lazy { mockk<BackupInteractor>() }
    private val userInteractor =
        lazy {
            mockk<UserInteractor> {
                every { getCurrentUser() } returns flowOf(mockk())
            }
        }
    private val currencyInteractor = lazy { mockk<CurrencyInteractor>() }
    private val currencyUpdateObserver = lazy { mockk<CurrencyUpdateObserver>() }
    private val geocodingInteractor = lazy { mockk<GeocodingInteractor>(relaxed = true) }
    private val locationProvider =
        lazy {
            mockk<LocationProvider> {
                coEvery { getCurrentLocation(any()) } returns emptyFlow()
            }
        }

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns firebaseAuth
        every { firebaseAuth.currentUser } returns null
        every { preferencesStore.uid } returns emptyFlow()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(FirebaseAuth::class)
    }

    @Test
    fun `creating view model leaves optional services uninitialized`() =
        runTest {
            createViewModel()
            advanceUntilIdle()

            assertFalse(backupInteractor.isInitialized())
            assertFalse(userInteractor.isInitialized())
            assertFalse(currencyInteractor.isInitialized())
            assertFalse(currencyUpdateObserver.isInitialized())
            assertFalse(geocodingInteractor.isInitialized())
            assertFalse(locationProvider.isInitialized())
        }

    @Test
    fun `signed out startup does not create user or currency services`() =
        runTest {
            every { preferencesStore.uid } returns flowOf(null)

            val viewModel = createViewModel()
            advanceUntilIdle()

            assertEquals(MainActivityUiState.Success.Auth, viewModel.uiStateFlow.value)
            assertFalse(userInteractor.isInitialized())
            assertFalse(currencyInteractor.isInitialized())
            assertFalse(backupInteractor.isInitialized())
        }

    @Test
    fun `signed in startup requests the user but leaves currency service deferred`() =
        runTest {
            every { preferencesStore.uid } returns flowOf("startup-test-user")
            every { firebaseAuth.currentUser } returns mockk { every { uid } returns "startup-test-user" }

            val viewModel = createViewModel()
            advanceUntilIdle()

            assertTrue(viewModel.uiStateFlow.value is MainActivityUiState.Success.Main)
            assertTrue(userInteractor.isInitialized())
            assertFalse(currencyInteractor.isInitialized())
            assertFalse(backupInteractor.isInitialized())
        }

    @Test
    fun `location provider is requested only for location events`() =
        runTest {
            val viewModel = createViewModel()
            assertFalse(locationProvider.isInitialized())

            viewModel.onLocationRequest(emptyList())
            advanceUntilIdle()

            assertTrue(locationProvider.isInitialized())
            coVerify(exactly = 1) { locationProvider.value.getCurrentLocation(emptyList()) }
            assertFalse(backupInteractor.isInitialized())
            assertFalse(currencyUpdateObserver.isInitialized())
            assertFalse(geocodingInteractor.isInitialized())
        }

    @Test
    fun `local currency event initializes geocoding without other optional services`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.updateLocalCurrency("USD")
            advanceUntilIdle()

            assertTrue(geocodingInteractor.isInitialized())
            coVerify(exactly = 1) { geocodingInteractor.value.updateLocalCurrency("USD") }
            assertFalse(backupInteractor.isInitialized())
            assertFalse(currencyUpdateObserver.isInitialized())
            assertFalse(locationProvider.isInitialized())
        }

    private fun createViewModel() =
        MainViewModel(
            ioDispatcher = dispatcher,
            analyticsSender = mockk(),
            preferencesStore = preferencesStore,
            backupInteractor = backupInteractor,
            userInteractor = userInteractor,
            spendingWidgetStateObserver = mockk(relaxUnitFun = true),
            categoryInteractor = lazy { mockk() },
            currencyUpdateObserver = currencyUpdateObserver,
            currencyInteractor = currencyInteractor,
            geocodingInteractor = geocodingInteractor,
            locationProvider = locationProvider,
        )
}
