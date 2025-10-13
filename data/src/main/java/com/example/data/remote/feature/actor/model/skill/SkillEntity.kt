package com.example.data.remote.feature.actor.model.skill

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SkillEntity(
    @SerialName("skill_id")
    val skillId: Long,

    val name: String,
    val description: String,

    @SerialName("max_skill_level")
    val maxSkillLevel: Int,

    @SerialName("skill_element_type")
    val skillElementType: String,

    @SerialName("activation_type")
    val activationType: String,

    @SerialName("mp_usage")
    val mpUsage: Int,

    @SerialName("mp_usage_decrease_amount")
    val mpUsageDecreaseAmount: Int,

    val multiplier: Float,

    @SerialName("multiplier_increase_amount")
    val multiplierIncreaseAmount: Float,

    @SerialName("hit_rate")
    val hitRate: Float,

    @SerialName("prerequisite_skills")
    val prerequisiteSkills: List<Long>
)
