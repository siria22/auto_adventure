package com.example.data.remote.feature.inventory.repository

import com.example.data.remote.feature.inventory.dao.InventoryItemDao
import com.example.data.remote.feature.inventory.toDomain
import com.example.data.remote.feature.inventory.toEntity
import com.example.domain.model.feature.inventory.InventoryItem
import com.example.domain.repository.feature.inventory.InventoryRepository
import javax.inject.Inject

class InventoryRepositoryImpl @Inject constructor(
    private val inventoryItemDao: InventoryItemDao
) : InventoryRepository {
    override suspend fun insertInventory(inventoryItem: InventoryItem) {
        inventoryItemDao.insert(inventoryItem.toEntity())
    }

    override suspend fun getAllInventories(): List<InventoryItem> {
        return inventoryItemDao.getAllInventories().map { it.toDomain() }
    }

    override suspend fun getInventoryByPartyId(partyId: Long): List<InventoryItem> {
        return inventoryItemDao.getInventoryByPartyId(partyId).map { it.toDomain() }
    }

}
