package com.example.data.remote.feature.dungeon.dao

import android.content.Context
import com.example.data.remote.feature.DUNGEON_ASSET_ROUTE
import com.example.data.remote.feature.dungeon.model.DungeonEventTableEntity
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DungeonEventTableProvider @Inject constructor(
    private val context: Context
) {
    private val events: List<DungeonEventTableEntity> by lazy {
        parseEvents()
    }

    private fun parseEvents(): List<DungeonEventTableEntity> {
        val jsonString =
            context.assets.open("$DUNGEON_ASSET_ROUTE/DungeonEventTable.json").bufferedReader()
                .use { it.readText() }
        return Json.decodeFromString<List<DungeonEventTableEntity>>(jsonString)
    }

    fun getAll(): List<DungeonEventTableEntity> {
        return events
    }

    fun getByDungeonAndFloor(dungeonId: Int, floor: Int): Result<DungeonEventTableEntity> {
        return events.find { it.dungeonId == dungeonId && it.floor == floor }?.let {
            Result.success(it)
        } ?: Result.failure(NoSuchElementException("Events for dungeon $dungeonId, floor $floor not found"))
    }
}
