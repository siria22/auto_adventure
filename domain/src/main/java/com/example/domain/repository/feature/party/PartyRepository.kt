package com.example.domain.repository.feature.party

import com.example.domain.model.feature.party.Party

interface PartyRepository {
    suspend fun insertParty(party: Party): Result<Unit>
    suspend fun getAllParties(): List<Party>
    suspend fun getPartyById(id: Long): Party?
    suspend fun getPartyByName(name: String): Party?
}