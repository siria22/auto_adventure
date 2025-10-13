package com.example.data.remote.feature.inventory.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseEquipEntity(
    @SerialName("equip_id")
    val equipId: Long,

    val category: String,
    val name: String,
    val description: String,

    @SerialName("base_required_strength")
    val baseRequiredStrength: Long,

    @SerialName("base_required_agility")
    val baseRequiredAgility: Long,

    @SerialName("base_required_intelligence")
    val baseRequiredIntelligence: Long,

    @SerialName("base_required_luck")
    val baseRequiredLuck: Long,

    @SerialName("increase_stat")
    val increaseStat: String,

    @SerialName("increase_amount")
    val increaseAmount: Long,

    @SerialName("max_reinforcement")
    val maxReinforcement: Int,

    @SerialName("buy_price")
    val buyPrice: Long,

    val weight: Double
)
