package com.example.data.remote.feature.inventory.model

import android.content.Context
import com.example.data.remote.feature.ITEM_ASSET_ROUTE
import kotlinx.serialization.json.Json
import javax.inject.Inject

class BaseEquipProvider @Inject constructor(
    private val context: Context
) {
    val baseEquips: List<BaseEquipEntity> by lazy {
        parseBaseEquips()
    }

    private fun parseBaseEquips(): List<BaseEquipEntity> {
        val jsonString = context.assets.open("${ITEM_ASSET_ROUTE}/BaseEquips.json").bufferedReader()
            .use { it.readText() }
        val json = Json { ignoreUnknownKeys = true }
        return json.decodeFromString<List<BaseEquipEntity>>(jsonString)
    }

    fun getBaseEquip(id: Long): Result<BaseEquipEntity> {
        return baseEquips.find { it.equipId == id }?.let {
            Result.success(it)
        } ?: Result.failure(NoSuchElementException("BaseEquip with id $id not found"))
    }
}
