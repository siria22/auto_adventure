package com.example.data.remote.feature.quest.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestEntity(
    @SerialName("quest_id")
    val questId: Long,

    @SerialName("dungeon_id")
    val dungeonId: Long,

    @SerialName("required_guild_level")
    val requiredGuildLevel: Int,

    @SerialName("quest_level")
    val questLevel: Int,

    val name: String,
    val description: String,

    @SerialName("reward_exp")
    val rewardExp: Long,

    @SerialName("reward_gold")
    val rewardGold: Long,

    @SerialName("reward_item_id")
    val rewardItemIds: List<Long>,

    @SerialName("reward_item_amount")
    val rewardItemAmounts: List<Int>,

    @SerialName("quest_type")
    val questType: String,

    @SerialName("clear_condition")
    val clearCondition: String
)
