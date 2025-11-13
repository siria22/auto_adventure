package com.example.domain.repository.feature.party

import com.example.domain.model.feature.party.Party
import com.example.domain.model.feature.party.PartyWithMembers

interface PartyRepository {
    suspend fun getPartyWithMembers(partyId: Long): PartyWithMembers?

    suspend fun insertParty(party: Party): Result<Unit>
    suspend fun getAllParties(): List<Party>
    suspend fun getPartyById(id: Long): Party?
    suspend fun getPartyByName(name: String): Party?
}