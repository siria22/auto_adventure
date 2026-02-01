package com.example.domain.model.feature.inventory

import com.example.domain.model.feature.types.ActorAttributeType
import com.example.domain.model.feature.types.EquipCategory

data class BaseEquip(
    val id: Long,
    val category: EquipCategory,
    val name: String,
    val description: String,
    val baseRequiredStrength: Long,
    val baseRequiredAgility: Long,
    val baseRequiredIntelligence: Long,
    val baseRequiredLuck: Long,
    val increaseStat: ActorAttributeType,
    val increaseAmount: Long,
    val maxReinforcement: Int,
    val buyPrice: Long,
    val weight: Double
)
