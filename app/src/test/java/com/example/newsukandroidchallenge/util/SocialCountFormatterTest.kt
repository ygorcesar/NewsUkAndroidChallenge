package com.example.newsukandroidchallenge.util

import org.junit.Assert.assertEquals
import org.junit.Test

class SocialCountFormatterTest {

    @Test
    fun `format returns raw number when less than 1000`() {
        assertEquals("0", SocialCountFormatter.format(0))
        assertEquals("1", SocialCountFormatter.format(1))
        assertEquals("999", SocialCountFormatter.format(999))
    }

    @Test
    fun `format returns K suffix for thousands`() {
        assertEquals("1.5K", SocialCountFormatter.format(1_500))
        assertEquals("52.0K", SocialCountFormatter.format(52_000))
        assertEquals("999.9K", SocialCountFormatter.format(999_900))
    }

    @Test
    fun `format returns M suffix for millions`() {
        assertEquals("1.0M", SocialCountFormatter.format(1_000_000))
        assertEquals("1.5M", SocialCountFormatter.format(1_500_000))
        assertEquals("2.4M", SocialCountFormatter.format(2_400_000))
    }
}
