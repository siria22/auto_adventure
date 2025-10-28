package com.example.domain.model.feature.dungeon

data class DungeonMonsterTable(
    val dungeonId: Int,
    val floor: Int,
    val room: Int?,
    val monsterId: List<Int>
)
