package com.example.domain.model.feature.actor.actor

import com.example.domain.model.feature.actor.base.BaseSkill

data class ActorSkills(
    val baseSkill: BaseSkill,
    val reinforcementLevel: Long,
    val isEquipped: Boolean
)