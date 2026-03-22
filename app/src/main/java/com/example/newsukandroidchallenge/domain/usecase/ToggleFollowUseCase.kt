package com.example.newsukandroidchallenge.domain.usecase

import com.example.newsukandroidchallenge.domain.repository.UserRepository
import javax.inject.Inject

class ToggleFollowUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userId: Int) = repository.toggleFollow(userId)
}
