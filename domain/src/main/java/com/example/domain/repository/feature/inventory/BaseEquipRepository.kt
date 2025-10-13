package com.example.domain.repository.feature.inventory

import com.example.domain.model.feature.inventory.BaseEquip

interface BaseEquipRepository {
    suspend fun getBaseEquipList(): List<BaseEquip>
    suspend fun getBaseEquipById(equipId: Long): Result<BaseEquip>
}
