package com.smtm.application.domain

data class Violation(val path: Path, val code: Code) {

    @JvmInline
    value class Path(val path: String) {

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