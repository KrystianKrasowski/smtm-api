package com.smtm.core.domain

data class Violation(val path: Path, val code: Code, val parameters: Map<String, String> = emptyMap()) {

    @JvmInline
    value class Path(private val path: String) {

        fun toJsonPath() = path
    }

    enum class Code {
        EMPTY,
        ILLEGAL_CHARACTERS,
        NON_UNIQUE,
        UNKNOWN,
        INVALID
    }

    companion object {

        fun path(path: String): Path =
            Path(path)

        fun empty(path: Path): Violation =
            Violation(path, Code.EMPTY)

        fun empty(path: String): Violation =
            Violation(path(path), Code.EMPTY)

        fun nonUnique(path: String): Violation =
            Violation(path(path), Code.NON_UNIQUE)

        fun illegalCharacters(path: String, illegalCharacters: CharArray): Violation =
            Violation(
                path = path(path),
                code = Code.ILLEGAL_CHARACTERS,
                parameters = mapOf(
                    "illegal-characters" to illegalCharacters.joinToString(", ")
                )
            )

        fun unknown(path: Path): Violation =
            Violation(path, Code.UNKNOWN)
    }
}
