package com.example.data.remote.feature.party.repository

import com.example.data.remote.feature.party.dao.PartyActionDao
import com.example.data.remote.feature.party.toDomain
import com.example.data.remote.feature.party.toEntity
import com.example.domain.model.feature.party.PartyAction
import com.example.domain.repository.feature.party.PartyActionRepository
import javax.inject.Inject

class PartyActionRepositoryImpl @Inject constructor(
    private val partyActionDao: PartyActionDao
) : PartyActionRepository {
    override suspend fun insertAction(action: PartyAction) {
        partyActionDao.insert(action.toEntity())
    }

    override suspend fun getAllActions(): List<PartyAction> {
        return partyActionDao.getAllActions().map { it.toDomain() }
    }

    override suspend fun getActionsByPartyId(partyId: Long): List<PartyAction> {
        return partyActionDao.getActionsByPartyId(partyId).map { it.toDomain() }
    }

    override suspend fun getActionById(partyId: Long, index: Long): PartyAction? {
        return partyActionDao.getActionById(partyId, index)?.toDomain()
    }
}
