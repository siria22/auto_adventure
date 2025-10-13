package com.example.domain.repository.feature.inventory

import com.example.domain.model.feature.inventory.CustomizedEquip

interface CustomizedEquipRepository {
    suspend fun insertEquip(equip: CustomizedEquip)
    suspend fun getAllEquips(): List<CustomizedEquip>
    suspend fun getEquipById(id: Long): CustomizedEquip?
    suspend fun getEquipsByOwnerId(ownerId: Long): List<CustomizedEquip>
}