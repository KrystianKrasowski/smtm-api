package com.smtm.infrastructure.persistence.plans

import java.io.Serializable

internal data class PlanEntryId(val plan: String = "", val categoryId: String = "") : Serializable
