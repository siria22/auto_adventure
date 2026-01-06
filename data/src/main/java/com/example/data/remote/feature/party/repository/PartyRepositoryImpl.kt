package com.example.data.remote.feature.party.repository

import com.example.data.remote.feature.party.dao.PartyDao
import com.example.data.remote.feature.party.toDomain
import com.example.data.remote.feature.party.toEntity
import com.example.domain.exception.PartyFullException
import com.example.domain.model.feature.party.Party
import com.example.domain.repository.feature.party.PartyRepository
import com.example.domain.model.feature.party.PartyWithMembers
import javax.inject.Inject

class PartyRepositoryImpl @Inject constructor(
    private val partyDao: PartyDao
) : PartyRepository {
    override suspend fun insertParty(party: Party): Result<Unit> = runCatching {
        if (partyDao.getAllParties().size == 5) {
            throw PartyFullException()
        }
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
        return null
    }
}
