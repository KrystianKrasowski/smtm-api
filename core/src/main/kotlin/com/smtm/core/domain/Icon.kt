package com.smtm.core.domain

enum class Icon {
    FOLDER,
    HOUSE,
    PIGGY_BANK,
    LIGHTENING,
    SHOPPING_CART,
    SUNGLASSES,
    WALLET,
    BANK
    ;

    companion object {

        fun valueOfOrDefault(value: String, default: Icon = FOLDER): Icon =
            runCatching { valueOf(value) }
                .getOrNull()
                ?: default
    }
}
