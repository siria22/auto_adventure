package com.example.domain.model.feature.types

enum class LogCategory {
    ADVENTURE
}

fun String.toLogCategory() : LogCategory {
    return when(this) {
        "ADVENTURE" -> LogCategory.ADVENTURE
        else -> throw IllegalArgumentException("Invalid LogCategory: $this")
    }
}