package com.smtm.transactions.categories

import com.smtm.common.ConstraintViolation
import com.smtm.transactions.api.AcceptingCategory
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions.assertThat

class AcceptingCategoryAssert(category: AcceptingCategory?)
    : AbstractAssert<AcceptingCategoryAssert, AcceptingCategory>(category, AcceptingCategoryAssert::class.java) {

    fun isDeclined(): AcceptingCategoryAssert {
        isNotNull
        assertThat(actual).isInstanceOf(AcceptingCategory.Declined::class.java)
        return myself
    }

    fun containsViolation(violation: ConstraintViolation): AcceptingCategoryAssert {
        isDeclined()
        assertThat(actual.asDeclined().violations).contains(violation)
        return myself
    }
}

private fun AcceptingCategory?.asDeclined() = this as AcceptingCategory.Declined
