package com.smtm.application.v1

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.util.Currency

data class MoneyDto(
    @JsonProperty("amount") val amount: Int,
    @JsonProperty("currency") val currency: String
) {

    companion object {

        fun of(amount: BigDecimal, currencyCode: String): MoneyDto =
            Currency.getInstance(currencyCode)
                .defaultFractionDigits
                .let { amount * BigDecimal.TEN.pow(it) }
                .setScale(2)
                .toInt()
                .let { MoneyDto(it, currencyCode) }
    }
}
