package com.example.domain.repository.feature.party

import com.example.domain.model.feature.party.BaseParty

interface PartyRepository {
    suspend fun insertParty(baseParty: BaseParty): Result<Unit>
    suspend fun getAllParties(): List<BaseParty>
    suspend fun getPartyById(id: Long): BaseParty?
    suspend fun getPartyByName(name: String): BaseParty?
}