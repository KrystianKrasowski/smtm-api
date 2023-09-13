package com.smtm.infrastructure.persistence

import javax.money.CurrencyUnit
import javax.money.Monetary
import javax.money.MonetaryAmount
import org.javamoney.moneta.Money
import java.math.BigDecimal

internal fun MonetaryAmount.toCents(): Int =
    number
        .numberValue(BigDecimal::class.java)
        .multiply(BigDecimal.TEN.pow(currency.defaultFractionDigits))
        .intValueExact()

internal fun Int.toMonetaryAmount(currencyCode: String): MonetaryAmount =
    Monetary.getCurrency(currencyCode)
        .let { this.toMonetaryAmount(it) }

private fun Int.toMonetaryAmount(currency: CurrencyUnit): MonetaryAmount =
    toBigDecimal()
        .setScale(currency.defaultFractionDigits)
        .divide(BigDecimal.TEN.pow(currency.defaultFractionDigits))
        .setScale(currency.defaultFractionDigits)
        .let { Money.of(it, currency) }
