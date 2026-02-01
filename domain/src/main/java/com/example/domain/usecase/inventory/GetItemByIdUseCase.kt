package com.example.domain.usecase.inventory

import com.example.domain.model.feature.inventory.Item
import com.example.domain.repository.feature.inventory.ItemRepository
import javax.inject.Inject

class GetItemByIdUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke(itemId: Long): Result<Item> {
        return runCatching {
            itemRepository.getItemById(itemId).getOrThrow()
        }
    }
}