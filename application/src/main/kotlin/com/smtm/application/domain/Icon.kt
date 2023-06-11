package com.smtm.application.domain

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
    }
}