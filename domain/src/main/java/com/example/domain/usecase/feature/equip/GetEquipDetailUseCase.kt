package com.example.domain.usecase.feature.equip

import com.example.domain.model.feature.inventory.BaseEquip
import com.example.domain.model.feature.inventory.CustomizedEquip
import com.example.domain.repository.feature.inventory.BaseEquipRepository
import com.example.domain.repository.feature.inventory.CustomizedEquipRepository
import javax.inject.Inject

class GetEquipDetailUseCase @Inject constructor(
    private val baseEquipRepository: BaseEquipRepository,
    private val customizedEquipRepository: CustomizedEquipRepository
) {
    suspend operator fun invoke(equipId: Long): Result<Pair<CustomizedEquip, BaseEquip>> {
        return runCatching {
            val customized = customizedEquipRepository.getEquipById(equipId)
                ?: throw NoSuchElementException("장비를 찾을 수 없습니다.")

            val base = baseEquipRepository.getBaseEquipById(customized.equipId).getOrNull()
                ?: throw NoSuchElementException("베이스 장비 정보를 찾을 수 없습니다.")

            customized to base
        }
    }
}