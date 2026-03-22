package com.example.newsukandroidchallenge.domain.repository

import com.example.newsukandroidchallenge.domain.model.User

interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
    fun getFollowedUserIds(): Set<Int>
    fun toggleFollow(userId: Int)
}
