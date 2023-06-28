package com.smtm.application.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.smtm.application.HalCollection
import com.smtm.application.HalResource
import com.smtm.application.Link

interface CategoriesApi {

    fun getAll(): HalCollection<CategoryDto>

    fun save(category: NewCategoryDto): CategoryDto

    fun update(category: CategoryDto)

    fun delete(id: Long)
}

data class CategoryDto(
    @JsonProperty("_links") override val links: Map<String, Link> = emptyMap(),
    @JsonProperty("id") val id: Long,
    @JsonProperty("name") val name: String,
    @JsonProperty("icon") val icon: String
) : HalResource(links)

data class NewCategoryDto(
    @JsonProperty("name") val name: String = "",
    @JsonProperty("icon") val icon: String = "FOLDER"
)