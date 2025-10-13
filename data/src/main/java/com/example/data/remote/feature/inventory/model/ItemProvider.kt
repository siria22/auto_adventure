package com.example.data.remote.feature.inventory.model

import android.content.Context
import com.example.data.remote.feature.ITEM_ASSET_ROUTE
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ItemProvider @Inject constructor(
    private val context: Context
) {
    private val items: List<ItemEntity> by lazy {
        parseItems()
    }

    private fun parseItems(): List<ItemEntity> {
        val jsonString = context.assets.open("${ITEM_ASSET_ROUTE}/Items.json").bufferedReader()
            .use { it.readText() }
        val json = Json { ignoreUnknownKeys = true }
        return json.decodeFromString<List<ItemEntity>>(jsonString)
    }

    fun getAllItems(): List<ItemEntity> {
        return items
    }

    fun getItem(id: Long): Result<ItemEntity> {
        return items.find { it.itemId == id }?.let {
            Result.success(it)
        } ?: Result.failure(NoSuchElementException("Item with id $id not found"))
    }
}
