package com.example.domain.model.feature.dungeon

data class DungeonFloorWeight(
    val dungeonId: Int,
    val floor: Int,
    val room: Int,
    val monsterMaxWeight: Int,
    val lootMinWeight: Int,
    val lootMaxWeight: Int
)
