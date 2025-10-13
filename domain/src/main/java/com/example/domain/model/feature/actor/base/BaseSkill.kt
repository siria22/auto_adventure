package com.example.domain.model.feature.actor.base

import com.example.domain.model.feature.types.ActivationType
import com.example.domain.model.feature.types.SkillElementType

data class BaseSkill(
    val id: Long,
    val name: String,
    val description: String,
    val maxLevel: Int,
    val elementType: SkillElementType,
    val activationType: ActivationType,
    val requiredMp: Int,
    val multiplier: Float,
    val hitRate: Float,
    val prerequisiteSkillIds: List<Long>
)