package com.example.domain.model.feature.dungeon

data class DungeonMapAttribute(
    val dungeonId: Int,
    val floor: Int,
    val noneRate: Int,
    val battleRate: Int,
    val eventRate: Int
)
