package com.example.data.remote.feature.dungeon.dao

import android.content.Context
import com.example.data.remote.feature.DUNGEON_ASSET_ROUTE
import com.example.data.remote.feature.dungeon.model.DungeonMapAttributeEntity
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DungeonMapAttributesProvider @Inject constructor(
    private val context: Context
) {
    private val attributes: List<DungeonMapAttributeEntity> by lazy {
        parseAttributes()
    }

    private fun parseAttributes(): List<DungeonMapAttributeEntity> {
        val jsonString =
            context.assets.open("$DUNGEON_ASSET_ROUTE/DungeonMapAttributes.json").bufferedReader()
                .use { it.readText() }
        return Json.Default.decodeFromString<List<DungeonMapAttributeEntity>>(jsonString)
    }

    fun getAll(): List<DungeonMapAttributeEntity> {
        return attributes
    }

    fun getByDungeonAndFloor(dungeonId: Int, floor: Int): Result<DungeonMapAttributeEntity> {
        return attributes.find { it.dungeonId == dungeonId && it.floor == floor }?.let {
            Result.success(it)
        } ?: Result.failure(NoSuchElementException("Attribute for dungeon $dungeonId, floor $floor not found"))
    }
}