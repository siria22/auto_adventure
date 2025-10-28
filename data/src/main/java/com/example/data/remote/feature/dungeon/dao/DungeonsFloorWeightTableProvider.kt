package com.example.data.remote.feature.dungeon.dao

import android.content.Context
import com.example.data.remote.feature.DUNGEON_ASSET_ROUTE
import com.example.data.remote.feature.dungeon.model.DungeonFloorWeightEntity
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DungeonsFloorWeightTableProvider @Inject constructor(
    private val context: Context
) {
    private val weights: List<DungeonFloorWeightEntity> by lazy {
        parseWeights()
    }

    private fun parseWeights(): List<DungeonFloorWeightEntity> {
        val jsonString =
            context.assets.open("$DUNGEON_ASSET_ROUTE/DungeonsFloorWeightTable.json").bufferedReader()
                .use { it.readText() }
        return Json.decodeFromString<List<DungeonFloorWeightEntity>>(jsonString)
    }

    fun getAll(): List<DungeonFloorWeightEntity> {
        return weights
    }

    fun getByDungeonAndFloor(dungeonId: Int, floor: Int): List<DungeonFloorWeightEntity> {
        return weights.filter { it.dungeonId == dungeonId && it.floor == floor }
    }
}
