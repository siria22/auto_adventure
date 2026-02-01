package com.example.domain.usecase.feature.equip

import com.example.domain.model.feature.inventory.CustomizedEquip
import com.example.domain.repository.feature.guild.GuildRepository
import com.example.domain.repository.feature.inventory.CustomizedEquipRepository
import com.example.domain.usecase.inventory.AddGlobalItemUseCase
import javax.inject.Inject

class ReinforceEquipUseCase @Inject constructor(
    private val customizedEquipRepository: CustomizedEquipRepository,
    private val guildRepository: GuildRepository,
    private val getReinforceInfoUseCase: GetReinforceInfoUseCase,
    private val addGlobalItemUseCase: AddGlobalItemUseCase
) {
    suspend operator fun invoke(equipId: Long): Result<CustomizedEquip> {
        return runCatching {
            val info = getReinforceInfoUseCase(equipId).getOrThrow()

            if (!info.isAffordable) {
                throw IllegalStateException("재료가 부족합니다.")
            }

            guildRepository.updateGold(-info.requiredGold)

            addGlobalItemUseCase(1L, info.requiredMaterialId, -info.requiredMaterialAmount).getOrThrow()

            val newEquip = info.targetEquip.copy(reinforcement = info.nextLevel)
            customizedEquipRepository.insertEquip(newEquip)

            newEquip
        }
    }
}