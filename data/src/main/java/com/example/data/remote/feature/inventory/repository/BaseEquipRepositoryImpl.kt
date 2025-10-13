package com.example.data.remote.feature.inventory.repository

import com.example.data.remote.feature.inventory.model.BaseEquipProvider
import com.example.data.remote.feature.inventory.toDomain
import com.example.domain.model.feature.inventory.BaseEquip
import com.example.domain.repository.feature.inventory.BaseEquipRepository
import javax.inject.Inject

class BaseEquipRepositoryImpl @Inject constructor(
    private val baseEquipProvider: BaseEquipProvider
) : BaseEquipRepository {
    override suspend fun getBaseEquipList(): List<BaseEquip> {
        return baseEquipProvider.baseEquips.map { it.toDomain() }
    }

    override suspend fun getBaseEquipById(equipId: Long): Result<BaseEquip> {
        return baseEquipProvider.getBaseEquip(equipId).map { it.toDomain() }
    }
}
