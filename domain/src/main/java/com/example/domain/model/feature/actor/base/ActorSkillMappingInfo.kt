package com.example.domain.model.feature.actor.base

data class ActorSkillMappingInfo(
    val actorId: Long,
    val skillId: Long,
    val skillLevel: Long,
    val isEquipped: Boolean
)