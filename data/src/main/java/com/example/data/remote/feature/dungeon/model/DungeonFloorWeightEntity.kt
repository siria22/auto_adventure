package com.example.data.remote.feature.dungeon.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DungeonFloorWeightEntity(
    @SerialName("dungeon_id")
    val dungeonId: Int,
    @SerialName("floor")
    val floor: Int,
    @SerialName("room")
    val room: Int? = null,
    @SerialName("monster_max_weight")
    val monsterMaxWeight: Int,
    @SerialName("loot_min_weight")
    val lootMinWeight: Int,
    @SerialName("loot_max_weight")
    val lootMaxWeight: Int
)
