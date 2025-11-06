package com.example.domain.usecase.inventory

import com.example.domain.model.feature.inventory.InventoryItem
import com.example.domain.repository.feature.inventory.InventoryRepository
import javax.inject.Inject

class AddOrUpdateItemUseCase @Inject constructor(
    private val inventoryRepository: InventoryRepository
) {
    suspend operator fun invoke(partyId: Long, itemId: Long, amountToAdd: Long) {
        val existingItem = inventoryRepository.findItem(partyId, itemId)

        if (existingItem != null) {
            val newAmount = existingItem.amount + amountToAdd
            if (newAmount > 0) {
                val updatedItem = existingItem.copy(amount = newAmount)
                inventoryRepository.updateInventory(updatedItem)
            } else {
                inventoryRepository.deleteInventory(existingItem)
            }
        } else {
            if (amountToAdd > 0) {
                val newItem = InventoryItem(partyId = partyId, itemId = itemId, amount = amountToAdd)
                inventoryRepository.insertInventory(newItem)
            }
        }
    }
}