package com.d9tilov.android.user.domain.impl

import com.d9tilov.android.user.domain.contract.UserInteractor
import com.d9tilov.android.user.domain.contract.UserRepo
import com.d9tilov.android.user.domain.model.UserProfile
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UserInteractorImplTest {
    private val userRepo: UserRepo = mockk()
    private val interactor: UserInteractor by lazy { UserInteractorImpl(userRepo) }

    private val testUser =
        UserProfile(
            uid = "test-user-id",
            displayedName = "Test User",
            firstName = "Test",
            lastName = "User",
            photoUrl = "https://example.com/photo.jpg",
            showPrepopulate = false,
            fiscalDay = 1,
        )

    @Test
    fun `getCurrentUser should return user from repo`() =
        runTest {
            every { userRepo.getCurrentUser() } returns flowOf(testUser)

            val user = interactor.getCurrentUser().first()

            assertNotNull(user)
            assertEquals(testUser.uid, user?.uid)
            assertEquals(testUser.displayedName, user?.displayedName)
            assertEquals(testUser.firstName, user?.firstName)
        }

    @Test
    fun `getCurrentUser should return null when no user exists`() =
        runTest {
            every { userRepo.getCurrentUser() } returns flowOf(null)

            val user = interactor.getCurrentUser().first()

            assertNull(user)
        }

    @Test
    fun `getFiscalDay should return fiscal day from repo`() =
        runTest {
            coEvery { userRepo.getFiscalDay() } returns 15

            val result = interactor.getFiscalDay()

            assertEquals(15, result)
            coVerify(exactly = 1) { userRepo.getFiscalDay() }
        }

    @Test
    fun `showPrepopulate should return boolean from repo`() =
        runTest {
            coEvery { userRepo.showPrepopulate() } returns true

            val result = interactor.showPrepopulate()

            assertTrue(result)
            coVerify(exactly = 1) { userRepo.showPrepopulate() }
        }

    @Test
    fun `prepopulateCompleted should delegate to repo`() =
        runTest {
            coEvery { userRepo.prepopulateCompleted() } returns Unit

            interactor.prepopulateCompleted()

            coVerify(exactly = 1) { userRepo.prepopulateCompleted() }
        }

    @Test
    fun `createUser should create and return user from repo`() =
        runTest {
            coEvery { userRepo.create(testUser) } returns testUser

            val result = interactor.createUser(testUser)

            assertEquals(testUser.uid, result.uid)
            assertEquals(testUser.displayedName, result.displayedName)
            coVerify(exactly = 1) { userRepo.create(testUser) }
        }

    @Test
    fun `updateFiscalDay should update fiscal day in repo`() =
        runTest {
            coEvery { userRepo.updateFiscalDay(20) } returns Unit

            interactor.updateFiscalDay(20)

            coVerify(exactly = 1) { userRepo.updateFiscalDay(20) }
        }

    @Test
    fun `deleteUser should delegate to repo delete`() =
        runTest {
            coEvery { userRepo.delete() } returns Unit

            interactor.deleteUser()

            coVerify(exactly = 1) { userRepo.delete() }
        }
}
