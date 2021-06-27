package com.smtm.infrastructure.persistence.categories

import javax.persistence.*

@Entity
@Table(name = "categories")
data class Category(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    val parent: Long?,
    val name: String,
    val icon: String
)
