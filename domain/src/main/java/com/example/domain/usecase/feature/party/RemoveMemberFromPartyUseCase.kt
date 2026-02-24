package com.example.domain.usecase.feature.party

import com.example.domain.repository.feature.party.PartyMemberRepository
import javax.inject.Inject

class RemoveMemberFromPartyUseCase @Inject constructor(
    private val partyMemberRepository: PartyMemberRepository
) {
    suspend operator fun invoke(partyId: Long, actorId: Long): Result<Unit> {
        val member =
            partyMemberRepository.getMembersByPartyId(partyId).find { it.characterId == actorId }
                ?: return Result.failure(IllegalArgumentException("해당 파티원을 찾을 수 없습니다."))

        partyMemberRepository.deleteMember(member)

        if (member.isPartyLeader) {
            val nextLeader = partyMemberRepository.getNextLeader(partyId)

            if (nextLeader != null) {
                partyMemberRepository.updatePartyLeader(nextLeader.characterId, true)
            }
        }

        return Result.success(Unit)
    }
}