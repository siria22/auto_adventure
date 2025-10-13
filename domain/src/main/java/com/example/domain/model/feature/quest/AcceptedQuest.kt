package com.example.domain.model.feature.quest

data class AcceptedQuest(
    val acceptedQuestId: Long,
    val questId: Long,
    val partyId: Long,
    val isCleared: Boolean
)
