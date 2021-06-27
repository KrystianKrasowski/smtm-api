package com.smtm.transactions.categories

import com.smtm.common.*
import com.smtm.transactions.api.Category
import com.smtm.transactions.spi.CategoriesRepository

private const val ILLEGAL_CHARACTERS_PATTERN = "[A-Za-z0-9 ]+"

internal class NewCategoryValidator(private val repository: CategoriesRepository) {

    fun validate(category: Category): Validated<Category> = collectConstraintViolations(category)
        .takeIf { it.isNotEmpty() }
        ?.let { validationFailureOf(it) }
        ?: validationSuccessOf(category)

    private fun collectConstraintViolations(category: Category) = listOfNotNull(
        category.isNotUnique(),
        category.nameHasIllegalCharacters(),
        category.iconHasIllegalCharacters(),
        category.belongsToNonExistingParent()
    )

    private fun Category.isNotUnique() = createNotUniqueViolations(name)
        .takeIf { repository.hasRegisteredName(name) }

    private fun Category.nameHasIllegalCharacters() = createIllegalCharactersViolation("name", name)
        .takeUnless { ILLEGAL_CHARACTERS_PATTERN.toRegex().matches(name) }

    private fun Category.iconHasIllegalCharacters() = createIllegalCharactersViolation("icon", icon)
        .takeUnless { ILLEGAL_CHARACTERS_PATTERN.toRegex().matches(icon) }

    private fun Category.belongsToNonExistingParent() = parent
        ?.takeUnless { repository.hasRegisteredId(it) }
        ?.let { createNonExistingParentViolation(it) }

    private fun createNotUniqueViolations(name: String) = constraintViolationOf(
        property = "name",
        message = messageOf(
            pattern = "Category %name% already exists",
            parameters = mapOf(
                "name" to name
            )
        )
    )

    private fun createIllegalCharactersViolation(property: String, name: String) = constraintViolationOf(
        property = property,
        message = messageOf(
            pattern = "Category $property contains illegal characters: %chars%",
            parameters = mapOf(
                "chars" to name
                    .replace(ILLEGAL_CHARACTERS_PATTERN.toRegex(), "")
                    .toCharArray()
                    .distinct()
                    .joinToString(",")
            )
        )
    )

    private fun createNonExistingParentViolation(parentId: Long): ConstraintViolation {
        return constraintViolationOf(
            property = "parent",
            message = messageOf(
                pattern = "Category parent of id %id% does not exist",
                parameters = mapOf(
                    "id" to parentId.toString()
                )
            )
        )
    }
}
