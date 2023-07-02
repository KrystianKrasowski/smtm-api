package com.smtm.application.domain

interface Aggregate<ID> {

    val id: ID
    val version: Version
}