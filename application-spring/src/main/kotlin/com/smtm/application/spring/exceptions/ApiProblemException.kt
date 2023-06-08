package com.smtm.application.spring.exceptions

import com.smtm.application.v1.ApiProblemDto

class ApiProblemException(val dto: ApiProblemDto.Undefined) : RuntimeException()