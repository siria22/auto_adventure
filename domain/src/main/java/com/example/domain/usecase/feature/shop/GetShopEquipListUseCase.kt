package com.example.domain.usecase.feature.shop

import com.example.domain.model.feature.inventory.BaseEquip
import com.example.domain.repository.feature.inventory.BaseEquipRepository
import javax.inject.Inject

class GetShopEquipListUseCase @Inject constructor(
    private val baseEquipRepository: BaseEquipRepository
) {
    suspend operator fun invoke(): Result<List<BaseEquip>> {
        return runCatching {
            baseEquipRepository.getBaseEquipList().filter { it.buyPrice > 0 }
        }
    }
}