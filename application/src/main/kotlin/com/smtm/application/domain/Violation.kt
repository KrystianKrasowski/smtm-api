package com.smtm.application.domain

data class Violation(val path: Path, val code: Code, val parameters: Map<String, String> = emptyMap()) {

    @JvmInline
    value class Path(private val path: String) {

        fun toJsonPath() = path
    }

    enum class Code {
        EMPTY,
        ILLEGAL_CHARACTERS,
        NON_UNIQUE
    }
}

fun violationPathOf(path: String) = Violation.Path(path)

fun emptyViolationOf(path: Violation.Path) = Violation(
    path = path,
    code = Violation.Code.EMPTY
)

fun nonUniqueViolationOf(path: Violation.Path) = Violation(
    path = path,
    code = Violation.Code.NON_UNIQUE
)

fun illegalCharactersViolationOf(path: Violation.Path, illegalCharacters: CharArray) = Violation(
    path = path,
    code = Violation.Code.ILLEGAL_CHARACTERS,
    parameters = mapOf(
        "illegal-characters" to illegalCharacters.joinToString(", ")
    )
)