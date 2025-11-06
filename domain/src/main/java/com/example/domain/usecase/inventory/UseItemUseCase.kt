package com.example.domain.usecase.inventory

import com.example.domain.model.feature.actor.actor.Actor
import com.example.domain.model.feature.inventory.Item
import com.example.domain.repository.feature.inventory.InventoryRepository
import javax.inject.Inject

class UseItemUseCase @Inject constructor(
    private val inventoryRepository: InventoryRepository,
    private val addOrUpdateItemUseCase: AddOrUpdateItemUseCase,
) {
    suspend operator fun invoke(actor: Actor, item: Item): Result<Actor> {
        val inventoryItem = inventoryRepository.findItem(actor.id, item.id)
            ?: return Result.failure(Exception("해당 아이템을 소지하고 있지 않습니다."))

        if (inventoryItem.amount <= 0) {
            return Result.failure(Exception("아이템이 부족합니다."))
        }

        val updatedActor = actor.useItem(item)

        val updateResult = addOrUpdateItemUseCase(actor.id, item.id, -1)
        if (updateResult.isFailure) {
            return Result.failure(updateResult.exceptionOrNull() ?: Exception("아이템 수량 변경에 실패했습니다."))
        }

        return Result.success(updatedActor)
    }
}