package com.example.domain.usecase.feature.equip

import com.example.domain.model.feature.inventory.CustomizedEquip
import com.example.domain.repository.feature.guild.GuildRepository
import com.example.domain.repository.feature.inventory.CustomizedEquipRepository
import com.example.domain.repository.feature.inventory.InventoryRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

data class ReinforceInfo(
    val targetEquip: CustomizedEquip,
    val nextLevel: Long,
    val requiredGold: Long,
    val requiredMaterialId: Long,
    val requiredMaterialAmount: Long,
    val currentGold: Long,
    val currentMaterialAmount: Long
) {
    val isAffordable: Boolean
        get() = currentGold >= requiredGold && currentMaterialAmount >= requiredMaterialAmount
}

class GetReinforceInfoUseCase @Inject constructor(
    private val customizedEquipRepository: CustomizedEquipRepository,
    private val guildRepository: GuildRepository,
    private val inventoryRepository: InventoryRepository
) {
    suspend operator fun invoke(equipId: Long): Result<ReinforceInfo> {
        return runCatching {
            val targetEquip = customizedEquipRepository.getEquipById(equipId)
                ?: throw IllegalArgumentException("장비를 찾을 수 없습니다.")

            val currentLevel = targetEquip.reinforcement
            val nextLevel = currentLevel + 1

            val requiredGold = nextLevel * 500
            val requiredMaterialId = 3L //TODO 슬라임 점액 임시
            val requiredMaterialAmount = nextLevel * 2

            val currentGold = guildRepository.observeGold().first()
            val materialItem = inventoryRepository.findItem(1L, requiredMaterialId)
            val currentMaterialAmount = materialItem?.amount ?: 0L

            ReinforceInfo(
                targetEquip = targetEquip,
                nextLevel = nextLevel,
                requiredGold = requiredGold,
                requiredMaterialId = requiredMaterialId,
                requiredMaterialAmount = requiredMaterialAmount,
                currentGold = currentGold,
                currentMaterialAmount = currentMaterialAmount
            )
        }
    }
}