package com.smtm.security.api

interface Authorization {

    fun authorize(token: String): Token?
}
