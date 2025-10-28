package com.example.data.remote.feature.party.repository

import com.example.data.remote.feature.party.dao.PartyActionDao
import com.example.data.remote.feature.party.toDomain
import com.example.data.remote.feature.party.toEntity
import com.example.domain.model.feature.party.BasePartyAction
import com.example.domain.repository.feature.party.PartyActionRepository
import javax.inject.Inject

class PartyActionRepositoryImpl @Inject constructor(
    private val partyActionDao: PartyActionDao
) : PartyActionRepository {
    override suspend fun insertAction(action: BasePartyAction) {
        partyActionDao.insert(action.toEntity())
    }

    override suspend fun getAllActions(): List<BasePartyAction> {
        return partyActionDao.getAllActions().map { it.toDomain() }
    }

    override suspend fun getActionsByPartyId(partyId: Long): List<BasePartyAction> {
        return partyActionDao.getActionsByPartyId(partyId).map { it.toDomain() }
    }

    override suspend fun getActionById(partyId: Long, index: Long): BasePartyAction? {
        return partyActionDao.getActionById(partyId, index)?.toDomain()
    }
}
