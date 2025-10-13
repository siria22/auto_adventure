package com.example.data.remote.feature.quest

import com.example.data.remote.feature.quest.entity.AcceptedQuestEntity
import com.example.data.remote.feature.quest.model.QuestEntity
import com.example.domain.model.feature.quest.AcceptedQuest
import com.example.domain.model.feature.quest.Quest
import com.example.domain.model.feature.types.QuestType

fun AcceptedQuestEntity.toDomain(): AcceptedQuest {
    return AcceptedQuest(
        acceptedQuestId = this.acceptedQuestId,
        questId = this.questId,
        partyId = this.partyId,
        isCleared = this.isCleared
    )
}

fun AcceptedQuest.toEntity(): AcceptedQuestEntity {
    return AcceptedQuestEntity(
        acceptedQuestId = this.acceptedQuestId,
        questId = this.questId,
        partyId = this.partyId,
        isCleared = this.isCleared
    )
}

fun QuestEntity.toDomain(): Quest {
    return Quest(
        id = this.questId,
        dungeonId = this.dungeonId,
        requiredGuildLevel = this.requiredGuildLevel,
        questLevel = this.questLevel,
        name = this.name,
        description = this.description,
        rewardExp = this.rewardExp,
        rewardGold = this.rewardGold,
        rewardItemIds = this.rewardItemIds,
        rewardItemAmounts = this.rewardItemAmounts,
        questType = QuestType.valueOf(this.questType),
        clearCondition = this.clearCondition
    )
}
