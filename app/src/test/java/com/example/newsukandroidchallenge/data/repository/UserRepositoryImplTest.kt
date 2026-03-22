package com.example.newsukandroidchallenge.data.repository

import android.content.SharedPreferences
import com.example.newsukandroidchallenge.data.api.StackOverflowApi
import com.example.newsukandroidchallenge.data.model.UserDto
import com.example.newsukandroidchallenge.data.model.UsersResponse
import com.example.newsukandroidchallenge.fixtures.UserDtoFixture
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

private const val FOLLOWERS_KEY = "followed_users"

class UserRepositoryImplTest {

    private val api: StackOverflowApi = mockk()
    private val prefs: SharedPreferences = mockk()
    private val editor: SharedPreferences.Editor = mockk(relaxed = true)
    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setup() {
        every { prefs.edit() } returns editor
        every { editor.putStringSet(any(), any()) } returns editor
        repository = UserRepositoryImpl(api, prefs)
    }

    @Test
    fun `getUsers returns success with mapped users`() = runTest {
        // Given
        val expected = UserDtoFixture.get()
        stubApiGetUsers(listOf(expected))
        every { prefs.getStringSet(any(), any()) } returns emptySet()

        // When
        val result = repository.getUsers()

        // Then
        assertTrue(result.isSuccess)
        val users = result.getOrNull()!!
        val actual = users.first()
        assertEquals(1, users.size)
        assertEquals(expected.userId, actual.id)
        assertEquals(expected.displayName, actual.name)
        assertEquals(expected.reputation, actual.reputation)
        assertEquals(expected.profileImage, actual.profileImage)
        assertEquals(false, actual.isFollowed)
    }

    @Test
    fun `getUsers marks followed users correctly`() = runTest {
        // Given
        val expected = UserDtoFixture.get()
        stubApiGetUsers(listOf(expected))
        every { prefs.getStringSet(any(), any()) } returns setOf(expected.userId.toString())

        // When
        val result = repository.getUsers()

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()!!.first().isFollowed)
    }

    @Test
    fun `getUsers returns failure on api error`() = runTest {
        // Given
        coEvery {
            api.getUsers(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws IOException("Network error")
        every { prefs.getStringSet(any(), any()) } returns emptySet()

        // When
        val result = repository.getUsers()

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
    }

    @Test
    fun `getFollowedUserIds returns empty set when no followed users`() {
        // Given
        every { prefs.getStringSet(any(), any()) } returns emptySet()

        // When
        val result = repository.getFollowedUserIds()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getFollowedUserIds returns correct ids`() {
        // Given
        every { prefs.getStringSet(any(), any()) } returns setOf("1", "2", "3")

        // When
        val result = repository.getFollowedUserIds()

        // Then
        assertEquals(setOf(1, 2, 3), result)
    }

    @Test
    fun `toggleFollow adds user when not followed`() {
        // Given
        every { prefs.getStringSet(any(), any()) } returns emptySet()

        // When
        repository.toggleFollow(1)

        // Then
        verify { editor.putStringSet(FOLLOWERS_KEY, setOf("1")) }
        verify { editor.apply() }
    }

    @Test
    fun `toggleFollow removes user when already followed`() {
        // Given
        every { prefs.getStringSet(any(), any()) } returns setOf("1", "2")

        // When
        repository.toggleFollow(1)

        // Then
        verify { editor.putStringSet(FOLLOWERS_KEY, setOf("2")) }
        verify { editor.apply() }
    }

    private fun stubApiGetUsers(users: List<UserDto>) {
        coEvery {
            api.getUsers(any(), any(), any(), any(), any())
        } returns UsersResponse(users)
    }
}
