package com.example.data.remote.feature.dungeon.dao

import android.content.Context
import com.example.data.remote.feature.DUNGEON_ASSET_ROUTE
import com.example.data.remote.feature.dungeon.model.DungeonMonsterTableEntity
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DungeonMonsterTableProvider @Inject constructor(
    private val context: Context
) {
    private val monsters: List<DungeonMonsterTableEntity> by lazy {
        parseMonsters()
    }

    private fun parseMonsters(): List<DungeonMonsterTableEntity> {
        val jsonString =
            context.assets.open("$DUNGEON_ASSET_ROUTE/DungeonMonsterTable.json").bufferedReader()
                .use { it.readText() }
        return Json.decodeFromString<List<DungeonMonsterTableEntity>>(jsonString)
    }

    fun getAll(): List<DungeonMonsterTableEntity> {
        return monsters
    }

    fun getByDungeonAndFloor(dungeonId: Int, floor: Int): Result<DungeonMonsterTableEntity> {
        return monsters.find { it.dungeonId == dungeonId && it.floor == floor }?.let {
            Result.success(it)
        } ?: Result.failure(NoSuchElementException("Monsters for dungeon $dungeonId, floor $floor not found"))
    }
}
