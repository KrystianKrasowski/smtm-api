package com.smtm.core.domain.shared

internal fun Regex.extractIllegalCharactersFrom(text: String) = replace(text, "")
    .toCharArray()
    .distinct()
    .toCharArray()
