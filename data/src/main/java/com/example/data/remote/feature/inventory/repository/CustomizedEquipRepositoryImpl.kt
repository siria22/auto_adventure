package com.example.data.remote.feature.inventory.repository

import com.example.data.remote.feature.inventory.dao.CustomizedEquipDao
import com.example.data.remote.feature.inventory.toDomain
import com.example.data.remote.feature.inventory.toEntity
import com.example.domain.model.feature.inventory.CustomizedEquip
import com.example.domain.repository.feature.inventory.BaseEquipRepository
import com.example.domain.repository.feature.inventory.CustomizedEquipRepository
import javax.inject.Inject

class CustomizedEquipRepositoryImpl @Inject constructor(
    private val customizedEquipDao: CustomizedEquipDao,
    private val baseEquipRepository: BaseEquipRepository
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

    override suspend fun deleteEquip(equip: CustomizedEquip) {
        customizedEquipDao.delete(equip.toEntity())
    }

    override suspend fun getEquipsByOwnerId(ownerId: Long): List<CustomizedEquip> {
        val equips = customizedEquipDao.getEquipsByOwnerId(ownerId)

        if (equips.isEmpty()) {
            val allEquips = customizedEquipDao.getAllEquips()
            if (allEquips.isEmpty()) {
                val baseEquips = baseEquipRepository.getBaseEquipList()

                val baseOwnerId = if (ownerId == 0L) 1L else ownerId

                baseEquips.forEach { base ->
                    val targetOwnerId = if (base.id % 2 == 0L) 0L else baseOwnerId

                    val newEquip = CustomizedEquip(
                        id = 0,
                        ownerId = targetOwnerId,
                        equipId = base.id,
                        category = base.category,
                        customizedName = base.name,
                        requiredStrength = base.baseRequiredStrength,
                        requiredAgility = base.baseRequiredAgility,
                        requiredIntelligence = base.baseRequiredIntelligence,
                        requiredLuck = base.baseRequiredLuck,
                        increaseStat = base.increaseStat,
                        increaseAmount = base.increaseAmount,
                        reinforcement = 0,
                        modifiedSellPrice = base.buyPrice,
                        modifiedWeight = base.weight.toLong(),
                        rank = "Normal"
                    )
                    customizedEquipDao.insert(newEquip.toEntity())
                }

                return customizedEquipDao.getEquipsByOwnerId(ownerId).map { it.toDomain() }
            }
        }

        return equips.map { it.toDomain() }
    }
}