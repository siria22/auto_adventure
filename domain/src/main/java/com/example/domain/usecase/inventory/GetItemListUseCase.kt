package com.example.domain.usecase.inventory

import com.example.domain.model.feature.inventory.Item
import com.example.domain.repository.feature.inventory.ItemRepository
import javax.inject.Inject

class GetItemListUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke(): Result<List<Item>> {
        return runCatching {
            itemRepository.getItemList()
        }
    }
}