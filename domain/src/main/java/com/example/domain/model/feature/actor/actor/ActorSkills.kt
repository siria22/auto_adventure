package com.example.domain.model.feature.actor.actor

import com.example.domain.model.feature.actor.base.BaseSkill

data class ActorSkills(
    val baseSkill: BaseSkill,
    val reinforcementLevel: Long,
    val isEquipped: Boolean
) {
    companion object {
        fun empty() = ActorSkills(
            baseSkill = BaseSkill.empty(),
            reinforcementLevel = 0,
            isEquipped = true
        )
    }
}