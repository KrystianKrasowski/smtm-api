package com.smtm.application.domain.plans

@JvmInline
value class PlanId(val value: Long) {

    companion object {

        private const val UNSETTLED_ID = -1L
        val UNSETTLED = PlanId(UNSETTLED_ID)
    }
}

fun Long.toPlanId() = PlanId(this)

fun Int.toPlanId() = PlanId(this.toLong())