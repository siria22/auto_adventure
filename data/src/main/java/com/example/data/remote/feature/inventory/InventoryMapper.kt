package com.example.data.remote.feature.inventory

import com.example.data.remote.feature.inventory.entity.CustomizedEquipEntity
import com.example.data.remote.feature.inventory.entity.InventoryItemEntity
import com.example.data.remote.feature.inventory.model.BaseEquipEntity
import com.example.data.remote.feature.inventory.model.ItemEntity
import com.example.domain.model.feature.inventory.BaseEquip
import com.example.domain.model.feature.inventory.CustomizedEquip
import com.example.domain.model.feature.inventory.InventoryItem
import com.example.domain.model.feature.inventory.Item
import com.example.domain.model.feature.types.ActorAttributeType
import com.example.domain.model.feature.types.EquipCategory
import com.example.domain.model.feature.types.ItemCategory
import com.example.domain.model.feature.types.ItemEffectType
import com.example.domain.model.feature.types.ObtainMethod
import com.example.domain.model.feature.types.StatType

fun ItemEntity.toDomain(): Item {
    return Item(
        id = this.itemId,
        name = this.name,
        shortDescription = this.shortDesc,
        fullDescription = this.fullDesc,
        category = ItemCategory.valueOf(this.category),
        weight = this.weight,
        maxStackSize = this.maxStackSize,
        obtainMethods = this.obtainMethod.map { ObtainMethod.valueOf(it) },
        isSellable = this.isSellable,
        buyPrice = this.buyPrice,
        sellPrice = this.sellPrice,
        isUsable = this.isUsable,
        effectType = runCatching { ItemEffectType.valueOf(this.effectType) }.getOrDefault(ItemEffectType.UNKNOWN),
        effectAmount = this.effectAmount
    )
}

fun CustomizedEquipEntity.toDomain(): CustomizedEquip {
    return CustomizedEquip(
        id = this.id,
        ownerId = this.ownerId,
        equipId = this.equipId,
        category = EquipCategory.valueOf(this.category),
        customizedName = this.customizedName,
        requiredStrength = this.requiredStrength,
        requiredAgility = this.requiredAgility,
        requiredIntelligence = this.requiredIntelligence,
        requiredLuck = this.requiredLuck,
        increaseStat = ActorAttributeType.valueOf(this.increaseStat),
        increaseAmount = this.increaseAmount,
        reinforcement = this.reinforcement,
        modifiedSellPrice = this.modifiedSellPrice,
        modifiedWeight = this.modifiedWeight,
        rank = this.rank
    )
}

fun InventoryItemEntity.toDomain(): InventoryItem {
    return InventoryItem(
        partyId = this.partyId,
        itemId = this.itemId,
        amount = this.amount
    )
}

fun BaseEquipEntity.toDomain(): BaseEquip {
    return BaseEquip(
        id = this.equipId,
        category = EquipCategory.valueOf(this.category),
        name = this.name,
        description = this.description,
        baseRequiredStrength = this.baseRequiredStrength,
        baseRequiredAgility = this.baseRequiredAgility,
        baseRequiredIntelligence = this.baseRequiredIntelligence,
        baseRequiredLuck = this.baseRequiredLuck,
        increaseStat = StatType.valueOf(this.increaseStat),
        increaseAmount = this.increaseAmount,
        maxReinforcement = this.maxReinforcement,
        buyPrice = this.buyPrice,
        weight = this.weight
    )
}

fun InventoryItem.toEntity(): InventoryItemEntity {
    return InventoryItemEntity(
        partyId = this.partyId,
        itemId = this.itemId,
        amount = this.amount
    )
}

fun CustomizedEquip.toEntity(): CustomizedEquipEntity {
    return CustomizedEquipEntity(
        id = this.id,
        ownerId = this.ownerId,
        equipId = this.equipId,
        category = this.category.name,
        customizedName = this.customizedName,
        requiredStrength = this.requiredStrength,
        requiredAgility = this.requiredAgility,
        requiredIntelligence = this.requiredIntelligence,
        requiredLuck = this.requiredLuck,
        increaseStat = this.increaseStat.name,
        increaseAmount = this.increaseAmount,
        reinforcement = this.reinforcement,
        modifiedSellPrice = this.modifiedSellPrice,
        modifiedWeight = this.modifiedWeight,
        rank = this.rank
    )
}
