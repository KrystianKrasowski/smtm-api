package com.smtm.application.domain.plans

import com.smtm.application.World
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.categories.Categories
import io.cucumber.java.DataTableType
import org.javamoney.moneta.Money
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.money.MonetaryAmount

class NewPlanFactory(world: World) {

    private val categories: Categories =
        world.categoriesRepository
            .getCategories(world.ownerId)
            .getOrNull()
            ?: error("Cannot get categories aggregate")

    @DataTableType
    fun create(entry: Map<String, String>): NewPlan =
        NewPlan(
            definition = PlanDefinition(
                id = NumericId.UNSETTLED,
                name = entry.getValue("name"),
                period = entry.getPeriod()
            ),
            entries = entry.getEntries()
        )

    private fun Map<String, String>.getPeriod(): ClosedRange<LocalDateTime> =
        getLocalDateTime("start", LocalTime.MIN)..getLocalDateTime("end", LocalTime.MAX)

    private fun Map<String, String>.getLocalDateTime(key: String, time: LocalTime): LocalDateTime =
        LocalDate.parse(getValue(key)).atTime(time)

    private fun Map<String, String>.getEntries(): List<NewPlan.Entry> =
        getValue("entries")
            .split(',')
            .map { it.trim() }
            .map { it.toEntry() }

    private fun String.toEntry(): NewPlan.Entry =
        split('=', limit = 2)
            .map { it.trim() }
            .let { NewPlan.Entry(it[0].toCategoryId(), it[1].toMoney()) }

    private fun String.toCategoryId(): NumericId =
        categories.findByName(this)?.id ?: NumericId.UNSETTLED

    private fun String.toMoney(): MonetaryAmount =
        Money.parse(this)
}