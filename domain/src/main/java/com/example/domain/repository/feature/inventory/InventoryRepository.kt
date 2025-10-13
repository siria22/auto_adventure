package com.example.domain.repository.feature.inventory

import com.example.domain.model.feature.inventory.InventoryItem

interface InventoryRepository {
    suspend fun insertInventory(inventoryItem: InventoryItem)
    suspend fun getInventoryByPartyId(partyId: Long): List<InventoryItem>
    suspend fun getAllInventories(): List<InventoryItem>
}