package com.example.data.remote.feature.inventory.repository

import com.example.data.remote.feature.inventory.dao.InventoryItemDao
import com.example.data.remote.feature.inventory.toDomain
import com.example.data.remote.feature.inventory.toEntity
import com.example.data.remote.feature.party.dao.PartyDao
import com.example.data.remote.feature.party.entity.PartyEntity
import com.example.domain.model.feature.inventory.InventoryItem
import com.example.domain.repository.feature.inventory.InventoryRepository
import javax.inject.Inject

class InventoryRepositoryImpl @Inject constructor(
    private val inventoryItemDao: InventoryItemDao,
    private val partyDao: PartyDao
) : InventoryRepository {
    override suspend fun insertInventory(inventoryItem: InventoryItem) {
        inventoryItemDao.insert(inventoryItem.toEntity())
    }

    override suspend fun updateInventory(inventoryItem: InventoryItem) {
        inventoryItemDao.update(inventoryItem.toEntity())
    }

    override suspend fun deleteInventory(inventoryItem: InventoryItem) {
        inventoryItemDao.delete(inventoryItem.toEntity())
    }

    override suspend fun getAllInventories(): List<InventoryItem> {
        return inventoryItemDao.getAllInventories().map { it.toDomain() }
    }

    override suspend fun getInventoryByPartyId(partyId: Long): List<InventoryItem> {
        val party = partyDao.getPartyById(partyId)
        if (party == null) {
            partyDao.insert(
                PartyEntity(
                    partyId = partyId,
                    name = "공용 파티",
                    isOnAdventure = false,
                    adventureStartTime = null
                )
            )
        }

        val inventories = inventoryItemDao.getInventoryByPartyId(partyId)

        if (inventories.isEmpty()) {
            val seedItems = listOf(
                InventoryItem(partyId = partyId, itemId = 1L, amount = 10L),
                InventoryItem(partyId = partyId, itemId = 2L, amount = 1L),
                InventoryItem(partyId = partyId, itemId = 3L, amount = 25L),
            )

            seedItems.forEach { inventoryItemDao.insert(it.toEntity()) }

            return inventoryItemDao.getInventoryByPartyId(partyId).map { it.toDomain() }
        }

        return inventories.map { it.toDomain() }
    }

    override suspend fun findItem(partyId: Long, itemId: Long): InventoryItem? {
        return inventoryItemDao.findItem(partyId, itemId)?.toDomain()
    }

}
