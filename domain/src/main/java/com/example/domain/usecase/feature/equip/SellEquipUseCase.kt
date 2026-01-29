package com.example.domain.usecase.feature.equip

import com.example.domain.repository.feature.guild.GuildRepository
import com.example.domain.repository.feature.inventory.CustomizedEquipRepository
import javax.inject.Inject

class SellEquipUseCase @Inject constructor(
    private val customizedEquipRepository: CustomizedEquipRepository,
    private val guildRepository: GuildRepository
) {
    suspend operator fun invoke(equipId: Long): Result<Unit> {
        return runCatching {
            val equip = customizedEquipRepository.getEquipById(equipId)
                ?: throw IllegalArgumentException("장비를 찾을 수 없습니다.")

            val sellPrice = equip.modifiedSellPrice
            guildRepository.updateGold(sellPrice)
            customizedEquipRepository.deleteEquip(equip)
        }
    }
}