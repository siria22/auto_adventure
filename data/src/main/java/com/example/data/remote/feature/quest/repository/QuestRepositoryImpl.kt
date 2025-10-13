package com.example.data.remote.feature.quest.repository

import com.example.data.remote.feature.quest.model.QuestProvider
import com.example.data.remote.feature.quest.toDomain
import com.example.domain.model.feature.quest.Quest
import com.example.domain.repository.feature.quest.QuestRepository
import javax.inject.Inject

class QuestRepositoryImpl @Inject constructor(
    private val questProvider: QuestProvider
) : QuestRepository {
    override suspend fun getQuestList(): List<Quest> {
        return questProvider.quests.map { it.toDomain() }
    }

    override suspend fun getQuestById(questId: Long): Result<Quest> {
        return questProvider.getQuest(questId).map { it.toDomain() }
    }
}
