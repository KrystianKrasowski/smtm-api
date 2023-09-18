package com.smtm.application.assert

import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.Version
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.plans.Plan
import com.smtm.application.domain.toOwnerId
import com.smtm.application.domain.toVersion
import org.assertj.core.api.AbstractAssert
import org.javamoney.moneta.Money
import java.time.LocalDateTime

class PlanAssert(plan: Plan?) : AbstractAssert<PlanAssert, Plan>(plan, PlanAssert::class.java) {

    fun hasVersion(version: Int): PlanAssert {
        return hasVersion(version.toVersion())
    }

    fun hasVersion(version: Version): PlanAssert {
        isNotNull

        if (actual.version != version) {
            failWithMessage("Expected plan version to be $version, but was ${actual.version}")
        }

        return myself
    }

    fun hasOwner(id: Int): PlanAssert {
        return hasOwner(id.toOwnerId())
    }

    fun hasOwner(ownerId: OwnerId): PlanAssert {
        isNotNull

        if (actual.ownerId != ownerId) {
            failWithMessage("Expected plan owner id to be $ownerId, but was ${actual.ownerId}")
        }

        return myself
    }

    fun hasName(name: String): PlanAssert {
        isNotNull

        if (actual.name != name) {
            failWithMessage("Expected plan name to be $name, but was ${actual.name}")
        }

        return myself
    }

    fun hasStart(start: String): PlanAssert {
        return hasStart(LocalDateTime.parse(start))
    }

    fun hasStart(start: LocalDateTime): PlanAssert {
        isNotNull

        if (actual.start != start) {
            failWithMessage("Expected plan start to be $start, but was ${actual.start}")
        }

        return myself
    }

    fun hasEnd(end: String): PlanAssert {
        return hasEnd(LocalDateTime.parse(end))
    }

    fun hasEnd(end: LocalDateTime): PlanAssert {
        isNotNull

        if (actual.end != end) {
            failWithMessage("Expected plan end to be $end, but was ${actual.end}")
        }

        return myself
    }

    fun hasNoPlannedCategories(): PlanAssert {
        isNotNull

        if (actual.entries.isNotEmpty()) {
            failWithMessage("Expected plan to has no planned categories, but was ${actual.entries}")
        }

        return myself
    }

    fun hasPlannedCategory(name: String, value: String): PlanAssert {
        isNotNull

        if (actual.getValueOf(name) != Money.parse(value)) {
            failWithMessage("Expected plan to contain planned category $name valued for $value")
        }

        return myself
    }

    fun hasOnlyPlannedCategory(name: String, value: String): PlanAssert {
        isNotNull

        if (actual.entries.size != 1 || actual.getValueOf(name) != Money.parse(value)) {
            failWithMessage("Expected plan to contain only $name planned valued for $value, but was ${actual.entries}")
        }

        return myself
    }

    fun hasAvailableCategory(category: Category): PlanAssert {
        isNotNull

        if (!actual.availableCategories.contains(category)) {
            failWithMessage("Expected plan to contain available category $category, but was ${actual.availableCategories}")
        }

        return myself
    }
}
