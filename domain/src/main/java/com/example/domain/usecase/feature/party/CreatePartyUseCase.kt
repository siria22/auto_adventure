package com.example.domain.usecase.feature.party

import com.example.domain.model.feature.party.Party
import com.example.domain.repository.feature.party.PartyRepository
import javax.inject.Inject

class CreatePartyUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val currentParties = partyRepository.getAllParties()

        if (currentParties.size > 3) {
            return Result.failure(IllegalStateException("파티는 최대 4개까지만 생성할 수 있습니다."))
        }

        var partyNumber = 1
        while (currentParties.any { it.name == "파티 $partyNumber" }) {
            partyNumber++
        }
        val newPartyName = "파티 $partyNumber"

        val newParty = Party(
            id = 0,
            name = newPartyName,
            isOnAdventure = false,
            adventureStartTime = null
        )

        return partyRepository.insertParty(newParty)
    }
}