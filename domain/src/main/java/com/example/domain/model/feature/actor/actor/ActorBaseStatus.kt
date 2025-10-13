package com.example.domain.model.feature.actor.actor

import com.example.domain.model.feature.actor.base.Personality
import com.example.domain.model.feature.types.StatType

data class ActorBaseStatus(
    val strength: Long,
    val agility: Long,
    val luck: Long,
    val intelligence: Long,
) {
    fun applyPersonalityModifier(personality: Personality): ActorBaseStatus {
        var modifiedStrength = strength
        var modifiedAgility = agility
        var modifiedLuck = luck
        var modifiedIntelligence = intelligence

        when (personality.increaseTarget) {
            StatType.STRENGTH -> modifiedStrength += personality.increaseAmount
            StatType.AGILITY -> modifiedAgility += personality.increaseAmount
            StatType.LUCK -> modifiedLuck += personality.increaseAmount
            StatType.INTELLIGENCE -> modifiedIntelligence += personality.increaseAmount
        }

        when (personality.decreaseTarget) {
            StatType.STRENGTH -> modifiedStrength -= personality.decreaseAmount
            StatType.AGILITY -> modifiedAgility -= personality.decreaseAmount
            StatType.LUCK -> modifiedLuck -= personality.decreaseAmount
            StatType.INTELLIGENCE -> modifiedIntelligence -= personality.decreaseAmount
        }

        return ActorBaseStatus(
            strength = modifiedStrength,
            agility = modifiedAgility,
            luck = modifiedLuck,
            intelligence = modifiedIntelligence
        )
    }
}