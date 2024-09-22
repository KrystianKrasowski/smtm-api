package com.smtm.api.v1

import org.javamoney.moneta.Money
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.math.BigDecimal

class MoneyDtoTest {

    @Test
    fun `should create dto from monetary amount`() {
        // when
        val money = Money.of(BigDecimal.valueOf(314.79), "PLN")
        val dto = MoneyDto.of(money)

        // then
        Assertions.assertEquals(MoneyDto(31479, "PLN"), dto)
    }

    @ParameterizedTest
    @CsvSource(
        "31479, PLN, 314.79",
        "31479, JPY, 31479"
    )
    fun `should return monetary amount from dto`(cents: Int, currencyCode: String, amount: BigDecimal) {
        // when
        val money = MoneyDto(cents, currencyCode).monetaryAmount

        // then
        Assertions.assertEquals(Money.of(amount, currencyCode), money)
    }
}
