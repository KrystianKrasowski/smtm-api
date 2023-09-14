package com.smtm.application.v1

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.javamoney.moneta.Money
import java.math.BigDecimal
import javax.money.CurrencyUnit
import javax.money.Monetary
import javax.money.MonetaryAmount

data class MoneyDto(
    @JsonProperty("amount") val amount: Int,
    @JsonProperty("currency") val currency: String
) {

    @JsonIgnore
    val monetaryAmount: MonetaryAmount =
        Monetary.getCurrency(currency)
            .createAmount(amount)

    companion object {

        fun of(value: MonetaryAmount): MoneyDto =
            MoneyDto(
                amount = value.getAmountAsCents(),
                currency = value.currency.currencyCode
            )
    }
}

private fun CurrencyUnit.createAmount(cents: Int): MonetaryAmount =
    cents
        .toBigDecimal()
        .setScale(defaultFractionDigits)
        .takeIf { defaultFractionDigits > 0 }
        ?.divide(BigDecimal.TEN.pow(defaultFractionDigits))
        ?.let { Money.of(it, this) }
        ?: Money.of(cents, this)

private fun MonetaryAmount.getAmountAsCents(): Int =
    number.numberValue(BigDecimal::class.java)
        .setScale(currency.defaultFractionDigits)
        .multiply(BigDecimal.TEN.pow(currency.defaultFractionDigits))
        .toInt()
