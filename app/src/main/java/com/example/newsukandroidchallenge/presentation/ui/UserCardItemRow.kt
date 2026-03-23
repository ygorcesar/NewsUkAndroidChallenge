package com.example.newsukandroidchallenge.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.newsukandroidchallenge.R
import com.example.newsukandroidchallenge.domain.model.User
import com.example.newsukandroidchallenge.ui.theme.NewsUkAndroidChallengeTheme

@Composable
fun UserCardItemRow(
    user: User,
    onToggleFollow: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = user.profileImage,
                contentDescription = stringResource(
                    R.string.profile_image_content_description,
                    user.name,
                ),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(R.string.reputation_format, user.reputationDisplayFormat),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            ToggleFollowButton(isFollowed = user.isFollowed, onToggleFollow = onToggleFollow)
        }
    }
}

@Composable
private fun ToggleFollowButton(isFollowed: Boolean, onToggleFollow: () -> Unit) {
    if (isFollowed) {
        OutlinedButton(onClick = onToggleFollow) {
            Text(stringResource(R.string.unfollow))
        }
    } else {
        Button(onClick = onToggleFollow) {
            Text(stringResource(R.string.follow))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserCardItemRowPreview() = NewsUkAndroidChallengeTheme {
    UserCardItemRow(
        user = User(
            id = 1,
            name = "Ygor Lima",
            reputation = 1454978,
            profileImage = null,
            isFollowed = false,
        ),
        onToggleFollow = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun UserCardItemRowFollowedPreview() = NewsUkAndroidChallengeTheme {
    UserCardItemRow(
        user = User(
            id = 1,
            name = "Ygor Lima",
            reputation = 1454978,
            profileImage = null,
            isFollowed = true,
        ),
        onToggleFollow = {},
    )
}