package com.smtm.application.domain

@JvmInline
value class NumericId private constructor(val value: Long) {

    fun isUnsettled(): Boolean =
        this == UNSETTLED

    fun isSettled(): Boolean =
        !isUnsettled()

    override fun toString(): String =
        value.toString()

    operator fun inc(): NumericId =
        of(value + 1)

    companion object {

        private const val UNSETTLED_ID = -1L
        val UNSETTLED = NumericId(UNSETTLED_ID)

        fun of(id: Long?): NumericId =
            id?.let { NumericId(it) } ?: UNSETTLED

        fun of(id: Int): NumericId =
            of(id.toLong())
    }
}