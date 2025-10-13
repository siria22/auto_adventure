package com.example.data.remote.feature.actor.model.personality

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonalityEntity(
    @SerialName("personality_id")
    val personalityId: Long,

    val name: String,

    @SerialName("increase_target")
    val increaseTarget: String,

    @SerialName("increase_amount")
    val increaseAmount: Long,

    @SerialName("decrease_target")
    val decreaseTarget: String,

    @SerialName("decrease_amount")
    val decreaseAmount: Long
)
