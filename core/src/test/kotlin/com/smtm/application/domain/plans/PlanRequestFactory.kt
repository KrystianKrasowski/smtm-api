package com.smtm.application.domain.plans

import com.smtm.application.World
import com.smtm.application.domain.Icon
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.categories.Categories
import com.smtm.application.domain.categories.Category
import io.cucumber.java.DataTableType
import javax.money.MonetaryAmount
import org.javamoney.moneta.Money
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class PlanRequestFactory(world: World) {

    private val categories: Categories =
        world.categoriesRepository
            .getCategories(world.ownerId)
            .getOrNull()
            ?: error("Cannot get categories aggregate")

    @DataTableType
    fun create(entry: Map<String, String>): PlanRequest =
        PlanRequest(
            definition = PlanDefinition(
                id = NumericId.UNSETTLED,
                name = entry.getValue("name"),
                period = entry.getPeriod()
            ),
            categories = entry.getEntries()
        )

    private fun Map<String, String>.getPeriod(): ClosedRange<LocalDateTime> =
        getLocalDateTime("start", LocalTime.MIN)..getLocalDateTime("end", LocalTime.MAX)

    private fun Map<String, String>.getLocalDateTime(key: String, time: LocalTime): LocalDateTime =
        LocalDate.parse(getValue(key)).atTime(time)

    private fun Map<String, String>.getEntries(): List<PlannedCategory> =
        getValue("entries")
            .split(',')
            .map { it.trim() }
            .map { it.toPlannedCategory() }

    private fun String.toPlannedCategory(): PlannedCategory =
        split('=', limit = 2)
            .map { it.trim() }
            .let { PlannedCategory(it[0].toCategory(), it[1].toMoney()) }

    private fun String.toCategory(): Category =
        categories.findByName(this) ?: Category(NumericId.UNSETTLED, this, Icon.FOLDER)

    private fun String.toMoney(): MonetaryAmount =
        Money.parse(this)
}
