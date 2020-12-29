package com.smtm.application.common.dto

import com.smtm.security.registration.ConstraintViolation

data class ViolationsProblemDto(val violations: List<ConstraintViolation>)
