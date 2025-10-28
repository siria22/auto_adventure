package com.example.data.remote.feature.dungeon.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DungeonMonsterTableEntity(
    @SerialName("dungeon_id")
    val dungeonId: Int,
    @SerialName("floor")
    val floor: Int,
    @SerialName("room")
    val room: Int?,
    @SerialName("monster_id")
    val monsterId: List<Int>
)
