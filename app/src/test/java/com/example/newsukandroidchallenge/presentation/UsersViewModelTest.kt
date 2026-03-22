package com.example.newsukandroidchallenge.presentation

import app.cash.turbine.test
import com.example.newsukandroidchallenge.domain.model.User
import com.example.newsukandroidchallenge.domain.usecase.GetUsersUseCase
import com.example.newsukandroidchallenge.domain.usecase.ToggleFollowUseCase
import com.example.newsukandroidchallenge.fixtures.UserFixture
import com.example.newsukandroidchallenge.presentation.model.UsersUiState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getUsersUseCase: GetUsersUseCase = mockk()
    private val toggleFollowUseCase: ToggleFollowUseCase = mockk(relaxed = true)
    private lateinit var viewModel: UsersViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading then Success when use case returns data`() = runTest {
        // Given
        val users = listOf(UserFixture.get(), UserFixture.get())
        stubGetUsers(users)

        // When
        viewModel = UsersViewModel(getUsersUseCase, toggleFollowUseCase)

        // Then
        viewModel.uiState.test {
            assertEquals(UsersUiState.Loading, awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            val successState = awaitItem()
            assertTrue(successState is UsersUiState.Success)
            assertEquals(users, (successState as UsersUiState.Success).users)
        }
    }

    @Test
    fun `initial state is Loading then Error when use case fails`() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery { getUsersUseCase() } returns Result.failure(exception)

        // When
        viewModel = UsersViewModel(getUsersUseCase, toggleFollowUseCase)

        // Then
        viewModel.uiState.test {
            assertEquals(UsersUiState.Loading, awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            val errorState = awaitItem()
            assertTrue(errorState is UsersUiState.Error)
            assertEquals(exception.message, (errorState as UsersUiState.Error).message)
        }
    }

    @Test
    fun `toggleFollow updates user follow status`() = runTest {
        // Given
        val users = listOf(UserFixture.get(), UserFixture.get())
        val user = users.first()
        stubGetUsers(users)
        every { toggleFollowUseCase(user.id) } returns Unit

        viewModel = UsersViewModel(getUsersUseCase, toggleFollowUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.toggleFollow(user.id)

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is UsersUiState.Success)
            val users = (state as UsersUiState.Success).users
            assertTrue(users.first { it.id == user.id }.isFollowed)
        }

        verify { toggleFollowUseCase(user.id) }
    }

    fun stubGetUsers(users: List<User>) {
        coEvery { getUsersUseCase() } returns Result.success(users)
    }
}
