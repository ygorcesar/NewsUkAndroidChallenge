package com.example.newsukandroidchallenge.domain.model

import androidx.compose.runtime.Immutable
import com.example.newsukandroidchallenge.util.SocialCountFormatter

@Immutable
data class User(
    val id: Int,
    val name: String,
    val reputation: Int,
    val profileImage: String?,
    val isFollowed: Boolean = false
) {
    val reputationDisplayFormat: String = SocialCountFormatter.format(reputation)
}
