package com.example.domain.model.feature.item.effect

import com.example.domain.model.feature.inventory.Item
import com.example.domain.model.feature.types.ItemEffectType

object ItemEffectFactory {

    fun createEffect(item: Item): ItemEffect? {
        return when (item.effectType) {
            ItemEffectType.HEAL -> HealEffect(item.effectAmount)
            ItemEffectType.MANA_RESTORE -> ManaRestoreEffect(item.effectAmount)
            ItemEffectType.RESURRECTION -> ResurrectionEffect(item.effectAmount)
            ItemEffectType.UNKNOWN -> null
        }
    }
}