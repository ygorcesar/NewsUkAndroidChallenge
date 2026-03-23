package com.example.newsukandroidchallenge.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsukandroidchallenge.domain.usecase.GetUsersUseCase
import com.example.newsukandroidchallenge.domain.usecase.ToggleFollowUseCase
import com.example.newsukandroidchallenge.presentation.model.UsersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val toggleFollowUseCase: ToggleFollowUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UsersUiState>(UsersUiState.Loading)
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = UsersUiState.Loading
            getUsersUseCase.invoke().fold(
                onSuccess = { users -> _uiState.value = UsersUiState.Success(users.toImmutableList()) },
                onFailure = { error ->
                    _uiState.value = UsersUiState.Error(error.message ?: "Failed to load users")
                }
            )
        }
    }

    fun toggleFollow(userId: Int) {
        toggleFollowUseCase.invoke(userId)
        val currentState = _uiState.value
        if (currentState is UsersUiState.Success) {
            _uiState.update {
                UsersUiState.Success(
                    currentState.users.map { user ->
                        if (user.id == userId) {
                            user.copy(isFollowed = !user.isFollowed)
                        } else {
                            user
                        }
                    }.toImmutableList()
                )
            }
        }
    }
}
