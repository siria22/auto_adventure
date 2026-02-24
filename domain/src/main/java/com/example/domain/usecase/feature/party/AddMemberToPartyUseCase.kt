package com.example.domain.usecase.feature.party

import com.example.domain.model.feature.party.PartyMember
import com.example.domain.model.feature.types.PartyPosition
import com.example.domain.repository.feature.party.PartyMemberRepository
import javax.inject.Inject

class AddMemberToPartyUseCase @Inject constructor(
    private val partyMemberRepository: PartyMemberRepository
) {
    suspend operator fun invoke(partyId: Long, actorId: Long): Result<Unit> {
        val partyMembers = partyMemberRepository.getMembersByPartyId(partyId)

        if (partyMembers.size > 3) {
            return Result.failure(IllegalStateException("파티원은 최대 4명까지만 가능합니다."))
        }

        val existingMembership = partyMemberRepository.getPartyByCharacterId(actorId)
        if (existingMembership != null) {
            return Result.failure(IllegalStateException("이미 파티에 가입된 캐릭터입니다."))
        }

        val occupiedSlots = partyMemberRepository.getOccupiedSlots(partyId)
        val availableSlot = (0..3).firstOrNull { it !in occupiedSlots }
            ?: return Result.failure(IllegalStateException("빈 슬롯이 없습니다."))

        val isLeader = partyMembers.isEmpty()

        val newMember = PartyMember(
            characterId = actorId,
            partyId = partyId,
            isPartyLeader = isLeader,
            position = PartyPosition.FRONT,
            slotIndex = availableSlot
        )

        partyMemberRepository.insertMember(newMember)
        return Result.success(Unit)
    }
}