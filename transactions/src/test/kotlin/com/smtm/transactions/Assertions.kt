package com.smtm.transactions

import com.smtm.transactions.api.AcceptingCategory
import com.smtm.transactions.categories.AcceptingCategoryAssert

fun assertThat(acceptingCategory: AcceptingCategory) = AcceptingCategoryAssert(acceptingCategory)
