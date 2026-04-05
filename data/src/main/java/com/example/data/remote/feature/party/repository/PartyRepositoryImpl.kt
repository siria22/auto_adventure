package com.example.data.remote.feature.party.repository

import com.example.data.remote.feature.party.dao.PartyDao
import com.example.data.remote.feature.party.dao.PartyMemberDao
import com.example.data.remote.feature.party.toDomain
import com.example.data.remote.feature.party.toEntity
import com.example.domain.model.feature.party.Party
import com.example.domain.model.feature.party.PartyWithMembers
import com.example.domain.repository.feature.party.PartyRepository
import javax.inject.Inject

class PartyRepositoryImpl @Inject constructor(
    private val partyDao: PartyDao,
    private val partyMemberDao: PartyMemberDao
) : PartyRepository {
    override suspend fun insertParty(party: Party): Result<Long> = runCatching {
        partyDao.insert(party.toEntity())
    }.onFailure { ex ->
        return Result.failure(ex)
    }

    override suspend fun getAllParties(): List<Party> {
        return partyDao.getAllParties().map { it.toDomain() }
    }

    override suspend fun getPartyById(id: Long): Party? {
        return partyDao.getPartyById(id)?.toDomain()
    }

    override suspend fun getPartyByName(name: String): Party? {
        return partyDao.getPartyByName(name)?.toDomain()
    }

    override suspend fun getPartyWithMembers(partyId: Long): PartyWithMembers? {
        //TODO
        return null
    }

    override suspend fun deleteParty(party: Party) {
        partyDao.delete(party.toEntity())
    }
}
