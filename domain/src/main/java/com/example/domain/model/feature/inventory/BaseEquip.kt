package com.example.domain.model.feature.inventory

import com.example.domain.model.feature.types.EquipCategory
import com.example.domain.model.feature.types.StatType

data class BaseEquip(
    val id: Long,
    val category: EquipCategory,
    val name: String,
    val description: String,
    val baseRequiredStrength: Long,
    val baseRequiredAgility: Long,
    val baseRequiredIntelligence: Long,
    val baseRequiredLuck: Long,
    val increaseStat: StatType,
    val increaseAmount: Long,
    val maxReinforcement: Int,
    val buyPrice: Long,
    val weight: Double
)
