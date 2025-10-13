package com.example.data.remote.feature.inventory.repository

import com.example.data.remote.feature.inventory.dao.CustomizedEquipDao
import com.example.data.remote.feature.inventory.toDomain
import com.example.data.remote.feature.inventory.toEntity
import com.example.domain.model.feature.inventory.CustomizedEquip
import com.example.domain.repository.feature.inventory.CustomizedEquipRepository
import javax.inject.Inject

class CustomizedEquipRepositoryImpl @Inject constructor(
    private val customizedEquipDao: CustomizedEquipDao
) : CustomizedEquipRepository {
    override suspend fun insertEquip(equip: CustomizedEquip) {
        customizedEquipDao.insert(equip.toEntity())
    }

    override suspend fun getAllEquips(): List<CustomizedEquip> {
        return customizedEquipDao.getAllEquips().map { it.toDomain() }
    }

    override suspend fun getEquipById(id: Long): CustomizedEquip? {
        return customizedEquipDao.getEquipById(id)?.toDomain()
    }

    override suspend fun getEquipsByOwnerId(ownerId: Long): List<CustomizedEquip> {
        return customizedEquipDao.getEquipsByOwnerId(ownerId).map { it.toDomain() }
    }
}
