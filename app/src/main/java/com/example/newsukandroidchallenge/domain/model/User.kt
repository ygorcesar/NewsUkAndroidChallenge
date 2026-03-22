package com.example.newsukandroidchallenge.domain.model

data class User(
    val id: Int,
    val name: String,
    val reputation: Int,
    val profileImage: String?,
    val isFollowed: Boolean = false
)
