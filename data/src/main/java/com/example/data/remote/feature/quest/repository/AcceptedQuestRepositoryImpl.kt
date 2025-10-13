package com.example.data.remote.feature.quest.repository

import com.example.data.remote.feature.quest.dao.AcceptedQuestDao
import com.example.data.remote.feature.quest.toDomain
import com.example.data.remote.feature.quest.toEntity
import com.example.domain.model.feature.quest.AcceptedQuest
import com.example.domain.repository.feature.quest.AcceptedQuestRepository
import javax.inject.Inject

class AcceptedQuestRepositoryImpl @Inject constructor(
    private val acceptedQuestDao: AcceptedQuestDao
) : AcceptedQuestRepository {
    override suspend fun insertAcceptedQuest(quest: AcceptedQuest) {
        acceptedQuestDao.insert(quest.toEntity())
    }

    override suspend fun getAllAcceptedQuests(): List<AcceptedQuest> {
        return acceptedQuestDao.getAllAcceptedQuests().map { it.toDomain() }
    }

    override suspend fun getAcceptedQuestById(id: Long): AcceptedQuest? {
        return acceptedQuestDao.getAcceptedQuestById(id)?.toDomain()
    }

    override suspend fun getAcceptedQuestsByPartyId(partyId: Long): List<AcceptedQuest> {
        return acceptedQuestDao.getAcceptedQuestsByPartyId(partyId).map { it.toDomain() }
    }
}
