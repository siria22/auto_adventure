package com.example.domain.usecase.feature.party

import com.example.domain.model.feature.party.PartyMember
import com.example.domain.repository.feature.party.PartyMemberRepository
import javax.inject.Inject

class ReplacePartyMemberUseCase @Inject constructor(
    private val partyMemberRepository: PartyMemberRepository
) {
    suspend operator fun invoke(partyId: Long, oldMemberId: Long, newMemberId: Long): Result<Unit> {
        val members = partyMemberRepository.getMembersByPartyId(partyId)
        val oldMember = members.find { it.characterId == oldMemberId }

        if (oldMember == null) {
            return Result.failure(IllegalArgumentException("교체할 멤버가 파티에 없습니다."))
        }
        if (members.any { it.characterId == newMemberId }) {
            return Result.failure(IllegalArgumentException("새 멤버는 이미 파티에 있습니다."))
        }

        partyMemberRepository.deleteMember(oldMember)

        val newMember = PartyMember(
            characterId = newMemberId,
            partyId = partyId,
            isPartyLeader = oldMember.isPartyLeader,
            position = oldMember.position,
            slotIndex = oldMember.slotIndex
        )
        partyMemberRepository.insertMember(newMember)

        return Result.success(Unit)
    }
}