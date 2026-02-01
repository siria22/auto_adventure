package com.example.domain.usecase.inventory

import com.example.domain.model.feature.inventory.InventoryItem
import com.example.domain.repository.feature.inventory.InventoryRepository
import com.example.domain.repository.feature.inventory.ItemRepository
import javax.inject.Inject
import kotlin.math.min

private const val MAX_STACK_AMOUNT = 99L

class AddGlobalItemUseCase @Inject constructor(
    private val inventoryRepository: InventoryRepository,
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke(partyId: Long, itemId: Long, amountToAdd: Long): Result<Long> {
        if (itemRepository.getItemById(itemId).isFailure) {
            return Result.failure(Exception("존재하지 않는 아이템입니다. ID: $itemId"))
        }

        val currentInventory = inventoryRepository.getInventoryByPartyId(partyId)
        val existingItem = currentInventory.find { it.itemId == itemId }
        val currentAmount = existingItem?.amount ?: 0

        val finalAmountToAdd = if (amountToAdd > 0) {
            val availableStack = MAX_STACK_AMOUNT - currentAmount
            if (availableStack <= 0) {
                return Result.failure(Exception("아이템 소지 한도(${MAX_STACK_AMOUNT}개)에 도달했습니다."))
            }
            min(amountToAdd, availableStack)
        } else {
            amountToAdd
        }

        val newAmount = currentAmount + finalAmountToAdd

        if (existingItem != null) {
            if (newAmount > 0) {
                val updatedItem = existingItem.copy(amount = newAmount)
                inventoryRepository.updateInventory(updatedItem)
            } else {
                inventoryRepository.deleteInventory(existingItem)
            }
        } else {
            if (finalAmountToAdd > 0) {
                val newItem = InventoryItem(partyId = partyId, itemId = itemId, amount = finalAmountToAdd)
                inventoryRepository.insertInventory(newItem)
            }
        }
        return Result.success(finalAmountToAdd)
    }
}