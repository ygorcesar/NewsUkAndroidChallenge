package com.example.newsukandroidchallenge.presentation.model

import com.example.newsukandroidchallenge.domain.model.User
import kotlinx.collections.immutable.ImmutableList

sealed interface UsersUiState {
    data object Loading : UsersUiState
    data class Success(val users: ImmutableList<User>) : UsersUiState
    data class Error(val message: String) : UsersUiState
}