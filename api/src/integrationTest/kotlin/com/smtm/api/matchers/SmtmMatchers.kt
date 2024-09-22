package com.smtm.api.matchers

import org.hamcrest.Matcher

fun matchesNamedPattern(pattern: String): Matcher<String> =
    MatchesNamedPattern(pattern)
