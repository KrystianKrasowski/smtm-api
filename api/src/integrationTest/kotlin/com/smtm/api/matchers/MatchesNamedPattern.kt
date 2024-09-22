package com.smtm.api.matchers

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class MatchesNamedPattern(private val namedPattern: String) : TypeSafeMatcher<String>() {

    override fun describeTo(description: Description) {
        description.appendText("with named pattern: $namedPattern")
    }

    override fun matchesSafely(value: String): Boolean {
        val regex = namedPattern
            .replace("%uuid%", PATTERNS.getValue("%uuid%"))
            .toRegex()

        return value.matches(regex)
    }

    companion object {

        private val PATTERNS = mapOf(
            "%uuid%" to "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
        )
    }
}
