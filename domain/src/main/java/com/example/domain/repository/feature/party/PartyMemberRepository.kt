package com.example.domain.repository.feature.party

import com.example.domain.model.feature.party.BasePartyMember

interface PartyMemberRepository {
    suspend fun insertMember(member: BasePartyMember)
    suspend fun getMembersByPartyId(partyId: Long): List<BasePartyMember>
    suspend fun getPartyByCharacterId(characterId: Long): BasePartyMember?
    suspend fun updatePartyLeader(actorId: Long, isPartyLeader: Boolean)
}
