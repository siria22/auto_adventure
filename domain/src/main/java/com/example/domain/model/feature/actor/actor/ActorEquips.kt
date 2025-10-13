package com.example.domain.model.feature.actor.actor

import com.example.domain.model.feature.inventory.CustomizedEquip
import com.example.domain.model.feature.types.ActorAttributeType

data class ActorEquips(
    val weapon: CustomizedEquip?,
    val sidearm: CustomizedEquip?,
    val accessory: CustomizedEquip?,
    val armor: CustomizedEquip?,
    val gloves: CustomizedEquip?,
    val shoes: CustomizedEquip?
) {
    fun getEquippedArmorCount(): Int {
        return listOfNotNull(weapon, sidearm, armor, gloves, shoes).size
    }

    fun getTotalDefenseAmount(): Long {
        val equippedArmors = listOfNotNull(
            weapon, sidearm, armor, gloves, shoes
        ).filter { it.increaseStat == ActorAttributeType.DEF }
        return equippedArmors.sumOf {
            it.getIncreasedAmount()
        }
    }
}