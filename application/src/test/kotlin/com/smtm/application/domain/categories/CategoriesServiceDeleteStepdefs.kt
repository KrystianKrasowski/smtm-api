package com.smtm.application.domain.categories

import com.smtm.application.World
import com.smtm.application.service.CategoriesService
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.*

class CategoriesServiceDeleteStepdefs(private val world: World) {

    private val service get() = CategoriesService(world.categoriesRepository)
    private var deleted = false
    private var problem: CategoriesProblem? = null

    @When("user deletes category of id {int}")
    fun `user deletes category of id N`(id: Int) {
        service.delete(id.toLong(), world.ownerId)
            .onRight { deleted = true }
            .onLeft { this.problem = it }
    }

    @Then("user category is deleted successfully")
    fun `user category is deleted successfully`() {
        assertThat(problem).isNull()
        assertThat(deleted).isTrue()
    }
}