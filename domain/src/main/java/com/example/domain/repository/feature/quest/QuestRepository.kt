package com.example.domain.repository.feature.quest

import com.example.domain.model.feature.quest.Quest

interface QuestRepository {
    suspend fun getQuestList(): List<Quest>
    suspend fun getQuestById(questId: Long): Result<Quest>
}
