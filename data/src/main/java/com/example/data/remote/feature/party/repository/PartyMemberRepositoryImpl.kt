package com.example.data.remote.feature.party.repository

import com.example.data.remote.feature.party.dao.PartyMemberDao
import com.example.data.remote.feature.party.toDomain
import com.example.data.remote.feature.party.toEntity
import com.example.domain.model.feature.party.BasePartyMember
import com.example.domain.repository.feature.party.PartyMemberRepository
import javax.inject.Inject

class PartyMemberRepositoryImpl @Inject constructor(
    private val partyMemberDao: PartyMemberDao
) : PartyMemberRepository {
    override suspend fun insertMember(member: BasePartyMember) {
        partyMemberDao.insert(member.toEntity())
    }

    override suspend fun getMembersByPartyId(partyId: Long): List<BasePartyMember> {
        return partyMemberDao.getMembersByPartyId(partyId).map { it.toDomain() }
    }

    override suspend fun getPartyByCharacterId(characterId: Long): BasePartyMember? {
        return partyMemberDao.getPartyByActorId(characterId)?.toDomain()
    }

    override suspend fun updatePartyLeader(actorId: Long, isPartyLeader: Boolean) {
        partyMemberDao.updatePartyLeader(actorId, isPartyLeader)
    }
}
