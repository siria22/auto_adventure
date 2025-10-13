package com.example.domain.model.feature.party

import java.util.Date

data class Party(
    val id: Long,
    val name: String,
    val isOnAdventure: Boolean,
    val adventureStartTime: Date?
)
