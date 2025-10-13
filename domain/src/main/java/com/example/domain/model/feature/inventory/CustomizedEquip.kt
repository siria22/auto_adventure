package com.example.domain.model.feature.inventory

import com.example.domain.model.feature.types.ActorAttributeType
import com.example.domain.model.feature.types.EquipCategory

data class CustomizedEquip(
    val id: Long,
    val ownerId: Long,
    val equipId: Long,
    val category: EquipCategory,
    val customizedName: String,
    val requiredStrength: Long,
    val requiredAgility: Long,
    val requiredIntelligence: Long,
    val requiredLuck: Long,
    val increaseStat: ActorAttributeType, // TODO : Attribute 단위 올릴건지 Stat 단위 올릴건지
    val increaseAmount: Long,
    val reinforcement: Long,
    val modifiedSellPrice: Long,
    val modifiedWeight: Long,
    val rank: String // TODO: Enum으로 변경
) {
    fun getIncreasedAmount(): Long {
        return increaseAmount * reinforcement
    }
}
