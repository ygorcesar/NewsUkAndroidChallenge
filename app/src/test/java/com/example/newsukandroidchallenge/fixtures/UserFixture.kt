package com.example.newsukandroidchallenge.fixtures

import com.example.newsukandroidchallenge.domain.model.User
import java.util.UUID
import kotlin.random.Random

object UserFixture {

    fun get(
        id: Int = Random.nextInt(),
        name: String = UUID.randomUUID().toString(),
        reputation: Int = Random.nextInt(),
        isFollowed: Boolean = false,
    ): User = User(
        id = id,
        name = name,
        reputation = reputation,
        profileImage = "https://example.com/avatar.jpg",
        isFollowed = isFollowed,
    )
}