package com.example.domain.usecase.feature.shop

import com.example.domain.model.feature.inventory.CustomizedEquip
import com.example.domain.repository.feature.guild.GuildRepository
import com.example.domain.repository.feature.inventory.BaseEquipRepository
import com.example.domain.repository.feature.inventory.CustomizedEquipRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class BuyEquipUseCase @Inject constructor(
    private val baseEquipRepository: BaseEquipRepository,
    private val customizedEquipRepository: CustomizedEquipRepository,
    private val guildRepository: GuildRepository
) {
    suspend operator fun invoke(ownerId: Long, baseEquipId: Long): Result<Unit> {
        return runCatching {
            val baseEquip = baseEquipRepository.getBaseEquipById(baseEquipId).getOrNull()
                ?: throw IllegalArgumentException("장비 정보를 찾을 수 없습니다.")

            val price = baseEquip.buyPrice
            val currentGold = guildRepository.observeGold().first()

            if (currentGold < price) {
                throw IllegalStateException("골드가 부족합니다.")
            }

            val newEquip = CustomizedEquip(
                id = 0L,
                ownerId = ownerId,
                equipId = baseEquip.id,
                category = baseEquip.category,
                customizedName = baseEquip.name,
                requiredStrength = baseEquip.baseRequiredStrength,
                requiredAgility = baseEquip.baseRequiredAgility,
                requiredIntelligence = baseEquip.baseRequiredIntelligence,
                requiredLuck = baseEquip.baseRequiredLuck,
                increaseStat = baseEquip.increaseStat,
                increaseAmount = baseEquip.increaseAmount,
                reinforcement = 0L,
                modifiedSellPrice = baseEquip.buyPrice / 4,
                modifiedWeight = baseEquip.weight.toLong(),
                rank = "Normal"
            )

            customizedEquipRepository.insertEquip(newEquip)
            guildRepository.updateGold(-price)
        }
    }
}