package com.smtm.core.domain.categories

import arrow.core.Nel
import com.smtm.core.domain.Violation

sealed interface CategoriesProblem {

    data class Violations(val violations: Nel<Violation>) : CategoriesProblem
    object Unknown : CategoriesProblem
    data class Other(val message: String) : CategoriesProblem
}
