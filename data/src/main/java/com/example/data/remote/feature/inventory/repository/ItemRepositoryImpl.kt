package com.example.data.remote.feature.inventory.repository

import com.example.data.remote.feature.inventory.model.ItemProvider
import com.example.data.remote.feature.inventory.toDomain
import com.example.domain.model.feature.inventory.Item
import com.example.domain.repository.feature.inventory.ItemRepository
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val itemProvider: ItemProvider
) : ItemRepository {
    override suspend fun getItemList(): List<Item> {
        return itemProvider.getAllItems().map { it.toDomain() }
    }

    override suspend fun getItemById(itemId: Long): Result<Item> {
        return itemProvider.getItem(itemId).map { it.toDomain() }
    }
}
