package com.example.domain.model.feature.dungeon

data class DungeonEventTable(
    val dungeonId: Int,
    val floor: Int,
    val eventId: List<Int>
)
