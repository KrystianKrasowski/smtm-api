package com.smtm.core.domain.categories

interface CategoriesProblem {

    data class Failure(val throwable: Throwable) : CategoriesProblem

    companion object {

        fun failure(throwable: Throwable): Failure =
            Failure(throwable)
    }
}
