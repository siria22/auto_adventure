package com.example.domain.model.feature.actor.base

import com.example.domain.model.feature.types.StatType

data class Personality(
    val id: Long,
    val name: String,
    val increaseTarget: StatType,
    val increaseAmount: Long,
    val decreaseTarget: StatType,
    val decreaseAmount: Long
)
