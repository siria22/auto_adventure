package com.example.data.remote.feature.dungeon.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DungeonMapAttributeEntity(
    @SerialName("dungeon_id")
    val dungeonId: Int,
    @SerialName("floor")
    val floor: Int,
    @SerialName("none")
    val noneRate: Int,
    @SerialName("battle_rate")
    val battleRate: Int,
    @SerialName("event_rate")
    val eventRate: Int
)
