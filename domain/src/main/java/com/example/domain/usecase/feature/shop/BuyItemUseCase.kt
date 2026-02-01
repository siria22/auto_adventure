package com.example.domain.usecase.feature.shop

import com.example.domain.repository.feature.guild.GuildRepository
import com.example.domain.repository.feature.inventory.ItemRepository
import com.example.domain.usecase.inventory.AddGlobalItemUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class BuyItemUseCase @Inject constructor(
    private val itemRepository: ItemRepository,
    private val guildRepository: GuildRepository,
    private val addGlobalItemUseCase: AddGlobalItemUseCase
) {
    suspend operator fun invoke(partyId: Long, itemId: Long, quantity: Int): Result<Unit> {
        return runCatching {
            val item = itemRepository.getItemById(itemId).getOrNull()
                ?: throw IllegalArgumentException("아이템을 찾을 수 없습니다.")

            val estimatedTotalPrice = item.buyPrice * quantity
            val currentGold = guildRepository.observeGold().first()

            if (currentGold < estimatedTotalPrice) {
                throw IllegalStateException("골드가 부족합니다.")
            }

            val actualAddedAmount = addGlobalItemUseCase(partyId, itemId, quantity.toLong()).getOrThrow()

            if (actualAddedAmount > 0) {
                val actualTotalPrice = item.buyPrice * actualAddedAmount
                guildRepository.updateGold(-actualTotalPrice)
            }
        }
    }
}