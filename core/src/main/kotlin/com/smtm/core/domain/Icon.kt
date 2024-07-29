package com.smtm.core.domain

enum class Icon {
    FOLDER,
    HOUSE,
    PIGGY_BANK,
    LIGHTENING,
    SHOPPING_CART,
    SUNGLASSES
    ;

    companion object {

        fun valueOfOrNull(value: String) = runCatching { Icon.valueOf(value) }
            .getOrNull()

        fun valueOfOrDefault(value: String, default: Icon = FOLDER): Icon =
            valueOfOrNull(value) ?: default
    }
}
