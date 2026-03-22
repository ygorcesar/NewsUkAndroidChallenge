package com.example.newsukandroidchallenge.presentation.model

import com.example.newsukandroidchallenge.domain.model.User

sealed interface UsersUiState {
    data object Loading : UsersUiState
    data class Success(val users: List<User>) : UsersUiState
    data class Error(val message: String) : UsersUiState
}