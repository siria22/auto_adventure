package com.example.domain.usecase.feature.equip

import com.example.domain.model.feature.inventory.BaseEquip
import com.example.domain.model.feature.inventory.CustomizedEquip
import com.example.domain.repository.feature.inventory.BaseEquipRepository
import com.example.domain.repository.feature.inventory.CustomizedEquipRepository
import javax.inject.Inject

data class AllEquipsData(
    val baseEquips: List<BaseEquip>,
    val customizedEquips: List<CustomizedEquip>
)

class GetAllEquipsUseCase @Inject constructor(
    private val baseEquipRepository: BaseEquipRepository,
    private val customizedEquipRepository: CustomizedEquipRepository
) {
    suspend operator fun invoke(): Result<AllEquipsData> {
        return runCatching {
            val baseEquips = baseEquipRepository.getBaseEquipList()

            //TODO 1L: 플레이어 소유, 0L: 미착용
            val playerEquips = customizedEquipRepository.getEquipsByOwnerId(1L)
            val unequippedEquips = customizedEquipRepository.getEquipsByOwnerId(0L)

            AllEquipsData(baseEquips, playerEquips + unequippedEquips)
        }
    }
}