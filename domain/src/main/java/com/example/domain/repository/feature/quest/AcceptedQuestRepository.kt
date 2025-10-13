package com.example.domain.repository.feature.quest

import com.example.domain.model.feature.quest.AcceptedQuest

interface AcceptedQuestRepository {
    suspend fun insertAcceptedQuest(quest: AcceptedQuest)
    suspend fun getAllAcceptedQuests(): List<AcceptedQuest>
    suspend fun getAcceptedQuestById(id: Long): AcceptedQuest?
    suspend fun getAcceptedQuestsByPartyId(partyId: Long): List<AcceptedQuest>
}