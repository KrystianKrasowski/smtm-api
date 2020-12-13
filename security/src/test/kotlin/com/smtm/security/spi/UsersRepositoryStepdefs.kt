package com.smtm.security.spi

import com.smtm.security.World
import com.smtm.security.registration.UserProfile
import com.smtm.security.registration.emailAddressOf
import com.smtm.security.registration.validUserProfileOf
import io.cucumber.datatable.DataTable
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Given

class UsersRepositoryStepdefs(private val world: World) {

    @Given("users repository contains")
    fun `user repository contains`(dataTable: DataTable) {
        world.userRepository.addUsers(dataTable.asList(UserProfile.Valid::class.java))
    }

    @DataTableType
    fun userProfileEntry(entry: Map<String, String>) = validUserProfileOf(entry.getValue("id").toLong(), emailAddressOf(entry.getValue("email")))
}
