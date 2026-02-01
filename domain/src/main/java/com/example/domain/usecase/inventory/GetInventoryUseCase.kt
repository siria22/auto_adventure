package com.example.domain.usecase.inventory

import com.example.domain.model.feature.inventory.InventoryItem
import com.example.domain.repository.feature.inventory.InventoryRepository
import javax.inject.Inject

class GetInventoryUseCase @Inject constructor(
    private val inventoryRepository: InventoryRepository
) {
    suspend operator fun invoke(partyId: Long): Result<List<InventoryItem>> {
        return runCatching {
            inventoryRepository.getInventoryByPartyId(partyId)
        }
    }
}