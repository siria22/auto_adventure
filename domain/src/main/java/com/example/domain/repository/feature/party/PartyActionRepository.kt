package com.example.domain.repository.feature.party

import com.example.domain.model.feature.party.PartyAction

interface PartyActionRepository {
    suspend fun insertAction(action: PartyAction)
    suspend fun getAllActions(): List<PartyAction>
    suspend fun getActionsByPartyId(partyId: Long): List<PartyAction>
    suspend fun getActionById(partyId: Long, index: Long): PartyAction?
}