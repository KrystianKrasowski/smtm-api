package com.smtm.application.domain.plans

sealed interface PlanSummariesProblem {

    object RepositoryFailure : PlanSummariesProblem
}