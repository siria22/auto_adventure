package com.example.domain.usecase.feature.party

import com.example.domain.repository.feature.party.PartyRepository
import javax.inject.Inject

class DisbandPartyUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) {
    suspend operator fun invoke(partyId: Long): Result<Unit> {
        val party = partyRepository.getPartyById(partyId)
            ?: return Result.failure(IllegalArgumentException("파티를 찾을 수 없습니다."))

        if (party.isOnAdventure) {
            return Result.failure(IllegalStateException("모험 중인 파티는 해체할 수 없습니다."))
        }

        partyRepository.deleteParty(party)
        return Result.success(Unit)
    }
}