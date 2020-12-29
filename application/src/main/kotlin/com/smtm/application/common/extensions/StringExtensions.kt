package com.smtm.application.common.extensions

import org.springframework.http.MediaType

fun String.toMediaType(): MediaType = MediaType.parseMediaType(this)
