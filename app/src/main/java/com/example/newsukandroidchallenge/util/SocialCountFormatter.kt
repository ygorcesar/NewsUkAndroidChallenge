package com.example.newsukandroidchallenge.util

object SocialCountFormatter {

    /**
     * @param count number of followers, reputation or any raw count.
     * @return formatted in social format
     * eg. 2.400.350 -> 2.4M
     * eg. 52.000 -> 52K
     */
    fun format(count: Int): String = when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
        else -> count.toString()
    }
}