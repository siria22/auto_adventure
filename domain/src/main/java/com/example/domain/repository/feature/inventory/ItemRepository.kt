package com.example.domain.repository.feature.inventory

import com.example.domain.model.feature.inventory.Item

interface ItemRepository {
    suspend fun getItemList(): List<Item>
    suspend fun getItemById(itemId: Long): Result<Item>
}
