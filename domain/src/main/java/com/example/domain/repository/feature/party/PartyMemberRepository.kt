package com.example.domain.repository.feature.party

import com.example.domain.model.feature.party.PartyMember

interface PartyMemberRepository {
    suspend fun insertMember(member: PartyMember)
    suspend fun getMembersByPartyId(partyId: Long): List<PartyMember>
    suspend fun getPartyByCharacterId(characterId: Long): PartyMember?
    suspend fun updatePartyLeader(actorId: Long, isPartyLeader: Boolean)
}
