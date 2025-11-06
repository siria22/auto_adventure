package com.example.domain.usecase.inventory

import com.example.domain.model.feature.inventory.InventoryItem
import com.example.domain.repository.feature.inventory.InventoryRepository
import javax.inject.Inject

class AddOrUpdateItemUseCase @Inject constructor(
    private val inventoryRepository: InventoryRepository
) {
    suspend operator fun invoke(partyId: Long, itemId: Long, amountToAdd: Long): Result<Unit> {
        val existingItem = inventoryRepository.findItem(partyId, itemId)
        val newAmount = (existingItem?.amount ?: 0) + amountToAdd
        if (newAmount > 99) {
            return Result.failure(Exception("아이템 최대 소지 개수(99개)를 초과합니다."))
        }

        if (existingItem != null) {
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
        return Result.success(Unit)
    }
}