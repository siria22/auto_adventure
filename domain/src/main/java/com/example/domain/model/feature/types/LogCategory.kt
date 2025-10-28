package com.example.domain.model.feature.types

enum class LogCategory {
    ADVENTURE,
    BATTLE
}

fun String.toLogCategory() : LogCategory {
    return when(this) {
        "ADVENTURE" -> LogCategory.ADVENTURE
        "BATTLE" -> LogCategory.BATTLE
        else -> throw IllegalArgumentException("Invalid LogCategory: $this")
    }
}