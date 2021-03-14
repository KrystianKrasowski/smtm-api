package com.smtm.transactions.categories

import com.smtm.common.ConstraintViolation
import com.smtm.common.constraintViolationOf
import com.smtm.common.messageOf
import com.smtm.transactions.api.Category
import com.smtm.transactions.spi.CategoriesRepository

private const val NamePattern = "[A-Za-z0-9 ]+"

internal class NewCategoryValidator(private val repository: CategoriesRepository) {

    fun validate(category: Category): List<ConstraintViolation> = listOfNotNull(
        category.isUnique(),
        category.hasIllegalCharacters()
    )

    private fun Category.isUnique() = createNotUniqueViolations(name)
        .takeIf { repository.hasRegisteredName(name) }

    private fun Category.hasIllegalCharacters() = createIllegalCharactersViolation(name)
        .takeUnless { NamePattern.toRegex().matches(name) }

    private fun createNotUniqueViolations(name: String) = constraintViolationOf(
        property = "name",
        message = messageOf(
            pattern = "Category %name% already exists",
            parameters = mapOf(
                "name" to name
            )
        )
    )

    private fun createIllegalCharactersViolation(name: String) = constraintViolationOf(
        property = "name",
        message = messageOf(
            pattern = "Category name contains illegal characters: %chars%",
            parameters = mapOf(
                "chars" to name
                    .replace(NamePattern.toRegex(), "")
                    .toCharArray()
                    .distinct()
                    .joinToString(",")
            )
        )
    )
}
