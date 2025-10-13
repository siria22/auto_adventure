package com.example.domain.model.feature.quest

import com.example.domain.model.feature.types.QuestType

data class Quest(
    val id: Long,
    val dungeonId: Long,
    val requiredGuildLevel: Int,
    val questLevel: Int,
    val name: String,
    val description: String,
    val rewardExp: Long,
    val rewardGold: Long,
    val rewardItemIds: List<Long>,
    val rewardItemAmounts: List<Int>,
    val questType: QuestType,
    val clearCondition: String
)
