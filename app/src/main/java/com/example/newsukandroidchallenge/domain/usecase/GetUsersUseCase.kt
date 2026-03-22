package com.example.newsukandroidchallenge.domain.usecase

import com.example.newsukandroidchallenge.domain.model.User
import com.example.newsukandroidchallenge.domain.repository.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<List<User>> = repository.getUsers()
}
