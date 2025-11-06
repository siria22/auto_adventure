package com.example.domain.model.feature.inventory

import com.example.domain.model.feature.types.ItemCategory
import com.example.domain.model.feature.types.ItemEffectType
import com.example.domain.model.feature.types.ObtainMethod

data class Item(
    val id: Long,
    val name: String,
    val shortDescription: String,
    val fullDescription: String,
    val category: ItemCategory,
    val weight: Double,
    val maxStackSize: Int,
    val obtainMethods: List<ObtainMethod>,
    val isSellable: Boolean,
    val buyPrice: Long,
    val sellPrice: Long,
    val isUsable: Boolean,
    val effectType: ItemEffectType,
    val effectAmount: Long
)
