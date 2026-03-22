package com.example.newsukandroidchallenge.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.newsukandroidchallenge.data.api.StackOverflowApi
import com.example.newsukandroidchallenge.domain.model.User
import com.example.newsukandroidchallenge.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: StackOverflowApi,
    private val prefs: SharedPreferences
) : UserRepository {

    override suspend fun getUsers(): Result<List<User>> = runCatching {
        val response = api.getUsers()
        val followedIds = getFollowedUserIds()
        val users = response.items.map { dto ->
            User(
                id = dto.userId,
                name = dto.displayName,
                reputation = dto.reputation,
                profileImage = dto.profileImage,
                isFollowed = dto.userId in followedIds
            )
        }
        users
    }.fold(
        onSuccess = { users -> Result.success(users) },
        onFailure = { error -> Result.failure(error) }
    )

    override fun getFollowedUserIds(): Set<Int> = prefs.getStringSet(KEY_FOLLOWED_USERS, emptySet())
        ?.mapNotNull { it.toIntOrNull() }
        ?.toSet()
        ?: emptySet()

    override fun toggleFollow(userId: Int) {
        val currentIds = getFollowedUserIds().toMutableSet()
        if (userId in currentIds) {
            currentIds.remove(userId)
        } else {
            currentIds.add(userId)
        }
        prefs.edit {
            putStringSet(KEY_FOLLOWED_USERS, currentIds.map { it.toString() }.toSet())
        }
    }

    companion object {
        private const val KEY_FOLLOWED_USERS = "followed_users"
    }
}
