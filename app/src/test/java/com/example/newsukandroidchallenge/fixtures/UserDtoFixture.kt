package com.example.newsukandroidchallenge.fixtures

import com.example.newsukandroidchallenge.data.model.UserDto
import java.util.UUID
import kotlin.random.Random

object UserDtoFixture {

    fun get(
        userId: Int = Random.nextInt(),
        displayName: String = UUID.randomUUID().toString(),
        reputation: Int = Random.nextInt(),
    ): UserDto = UserDto(
        userId = userId,
        displayName = displayName,
        reputation = reputation,
        profileImage = "https://example.com/avatar.jpg"
    )
}