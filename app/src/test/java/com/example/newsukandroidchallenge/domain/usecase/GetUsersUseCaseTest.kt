package com.example.newsukandroidchallenge.domain.usecase

import com.example.newsukandroidchallenge.domain.repository.UserRepository
import com.example.newsukandroidchallenge.fixtures.UserFixture
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GetUsersUseCaseTest {

    private val repository: UserRepository = mockk()
    private lateinit var useCase: GetUsersUseCase

    @Before
    fun setup() {
        useCase = GetUsersUseCase(repository)
    }

    @Test
    fun `invoke returns users from repository`() = runTest {
        // Given
        val testUsers = listOf(UserFixture.get(), UserFixture.get())
        coEvery { repository.getUsers() } returns Result.success(testUsers)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(testUsers, result.getOrNull())
        coVerify { repository.getUsers() }
    }

    @Test
    fun `invoke returns failure when repository fails`() = runTest {
        // Given
        val exception = IOException("Network error")
        coEvery { repository.getUsers() } returns Result.failure(exception)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
