package com.example.data.remote.feature.party.repository

import com.example.data.remote.feature.party.dao.PartyDao
import com.example.data.remote.feature.party.toDomain
import com.example.data.remote.feature.party.toEntity
import com.example.domain.exception.PartyFullException
import com.example.domain.model.feature.party.BaseParty
import com.example.domain.repository.feature.party.PartyRepository
import javax.inject.Inject

class PartyRepositoryImpl @Inject constructor(
    private val partyDao: PartyDao
) : PartyRepository {
    override suspend fun insertParty(baseParty: BaseParty): Result<Unit> = runCatching {
        if (partyDao.getAllParties().size == 5) {
            throw PartyFullException()
        }
        partyDao.insert(baseParty.toEntity())
    }.onFailure { ex ->
        return Result.failure(ex)
    }

    override suspend fun getAllParties(): List<BaseParty> {
        return partyDao.getAllParties().map { it.toDomain() }
    }

    override suspend fun getPartyById(id: Long): BaseParty? {
        return partyDao.getPartyById(id)?.toDomain()
    }

    override suspend fun getPartyByName(name: String): BaseParty? {
        return partyDao.getPartyByName(name)?.toDomain()
    }
}
