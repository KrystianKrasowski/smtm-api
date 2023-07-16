package com.smtm.application.domain.categories

import arrow.core.Nel
import com.smtm.application.domain.Violation

sealed interface CategoriesProblem {

    data class Violations(val violations: Nel<Violation>) : CategoriesProblem
    object Unknown : CategoriesProblem
    data class Other(val message: String) : CategoriesProblem
}