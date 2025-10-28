package com.example.domain.repository.feature.party

import com.example.domain.model.feature.party.BasePartyAction

interface PartyActionRepository {
    suspend fun insertAction(action: BasePartyAction)
    suspend fun getAllActions(): List<BasePartyAction>
    suspend fun getActionsByPartyId(partyId: Long): List<BasePartyAction>
    suspend fun getActionById(partyId: Long, index: Long): BasePartyAction?
}