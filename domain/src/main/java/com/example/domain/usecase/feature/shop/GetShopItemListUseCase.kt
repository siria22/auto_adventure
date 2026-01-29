package com.example.domain.usecase.feature.shop

import com.example.domain.model.feature.inventory.Item
import com.example.domain.repository.feature.inventory.ItemRepository
import javax.inject.Inject

class GetShopItemListUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke(): Result<List<Item>> {
        return runCatching {
            itemRepository.getItemList().filter { it.buyPrice > 0 }
        }
    }
}