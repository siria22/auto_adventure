package com.example.data.remote.feature.dungeon.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DungeonEventTableEntity(
    @SerialName("dungeon_id")
    val dungeonId: Int,
    @SerialName("floor")
    val floor: Int,
    @SerialName("event_id")
    val eventId: List<Int>
)
