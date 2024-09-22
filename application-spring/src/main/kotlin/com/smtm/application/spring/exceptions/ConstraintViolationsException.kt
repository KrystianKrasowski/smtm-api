package com.smtm.application.spring.exceptions

import com.smtm.application.v1.ApiProblemDto
import java.lang.RuntimeException

class ConstraintViolationsException(val dto: ApiProblemDto.ConstraintViolations) : RuntimeException()