package com.example.domain.usecase.inventory

import com.example.domain.model.feature.inventory.InventoryItem
import com.example.domain.repository.feature.inventory.InventoryRepository
import com.example.domain.repository.feature.inventory.ItemRepository
import com.example.domain.usecase.party.PartyUseCase
import javax.inject.Inject

class AddOrUpdateItemUseCase @Inject constructor(
    private val inventoryRepository: InventoryRepository,
    private val itemRepository: ItemRepository,
    private val partyUseCase: PartyUseCase
) {
    suspend operator fun invoke(partyId: Long, itemId: Long, amountToAdd: Long): Result<Unit> {
        val itemObject = itemRepository.getItemById(itemId).getOrNull()
            ?: return Result.failure(Exception("존재하지 않는 아이템입니다. ID: $itemId"))

        val maxWeight = partyUseCase.getMaxInventoryWeight(partyId)

        val currentInventory = inventoryRepository.getInventoryByPartyId(partyId)
        var currentWeight = 0.0
        for (invItem in currentInventory) {
            val itemDetail = itemRepository.getItemById(invItem.itemId).getOrNull()
            currentWeight += (itemDetail?.weight ?: 0.0) * invItem.amount
        }

        val availableWeight = maxWeight - currentWeight
        if (availableWeight <= 0) {
            return Result.failure(Exception("인벤토리 무게 한도를 초과하여 아이템을 추가할 수 없습니다."))
        }

        val maxAddableAmount = (availableWeight / itemObject.weight).toLong()
        if (maxAddableAmount <= 0) {
            return Result.failure(Exception("아이템 1개를 추가하기에도 무게가 부족합니다."))
        }

        val finalAmountToAdd = if (amountToAdd < maxAddableAmount) amountToAdd else maxAddableAmount

        val existingItem = currentInventory.find { it.itemId == itemId }
        val newAmount = (existingItem?.amount ?: 0) + finalAmountToAdd
        //todo 버려지는 아이템 처리 구현

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