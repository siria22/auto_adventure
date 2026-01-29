package com.example.domain.usecase.inventory

import com.example.domain.repository.feature.guild.GuildRepository
import com.example.domain.repository.feature.inventory.InventoryRepository
import com.example.domain.repository.feature.inventory.ItemRepository
import javax.inject.Inject

class SellItemUseCase @Inject constructor(
    private val inventoryRepository: InventoryRepository,
    private val itemRepository: ItemRepository,
    private val guildRepository: GuildRepository,
    private val addGlobalItemUseCase: AddGlobalItemUseCase
) {
    suspend operator fun invoke(partyId: Long, itemId: Long, quantity: Int): Result<Unit> {
        return runCatching {
            val inventoryItem = inventoryRepository.findItem(partyId, itemId)
                ?: throw IllegalStateException("아이템을 찾을 수 없습니다.")

            if (inventoryItem.amount < quantity) {
                throw IllegalArgumentException("판매 수량이 보유 수량보다 많습니다.")
            }

            val item = itemRepository.getItemById(itemId).getOrNull()
                ?: throw IllegalStateException("아이템 정보를 찾을 수 없습니다.")

            addGlobalItemUseCase(partyId, itemId, -quantity.toLong()).getOrThrow()

            val gainGold = item.sellPrice * quantity
            guildRepository.updateGold(gainGold)
        }
    }
}