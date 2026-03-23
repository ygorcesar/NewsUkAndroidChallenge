package com.example.newsukandroidchallenge.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newsukandroidchallenge.R
import com.example.newsukandroidchallenge.domain.model.User
import com.example.newsukandroidchallenge.presentation.UsersViewModel
import com.example.newsukandroidchallenge.presentation.model.UsersUiState
import com.example.newsukandroidchallenge.ui.theme.NewsUkAndroidChallengeTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun UsersScreen(
    modifier: Modifier = Modifier,
    viewModel: UsersViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier) {
        when (val state = uiState) {
            is UsersUiState.Loading -> LoadingState()
            is UsersUiState.Success -> UsersList(
                users = state.users,
                onToggleFollow = viewModel::toggleFollow,
            )

            is UsersUiState.Error -> ErrorState(
                message = state.message,
                onRetry = viewModel::loadUsers,
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
            )
            Button(onClick = onRetry) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}

@Composable
private fun UsersList(
    users: ImmutableList<User>,
    onToggleFollow: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(users, key = { it.id }) { user ->
            UserCardItemRow(
                user = user,
                onToggleFollow = { onToggleFollow(user.id) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingStatePreview() = NewsUkAndroidChallengeTheme {
    LoadingState()
}

@Preview(showBackground = true)
@Composable
private fun ErrorStatePreview() = NewsUkAndroidChallengeTheme {
    ErrorState(
        message = "Failed to load users",
        onRetry = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun UsersListPreview() = NewsUkAndroidChallengeTheme {
    UsersList(
        users = persistentListOf(
            User(
                id = 1,
                name = "Ygor Lima",
                reputation = 1454978,
                profileImage = null,
                isFollowed = false,
            ),
            User(
                id = 2,
                name = "Bruce Dickson",
                reputation = 1200000,
                profileImage = null,
                isFollowed = true,
            ),
            User(
                id = 3,
                name = "Bon Scoot",
                reputation = 950000,
                profileImage = null,
                isFollowed = false,
            ),
        ),
        onToggleFollow = {},
    )
}
