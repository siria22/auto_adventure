package com.example.domain.usecase.party

import com.example.domain.repository.feature.party.PartyRepository
import javax.inject.Inject

private const val BASE_WEIGHT = 50.0
private const val STRENGTH_WEIGHT_RATIO = 2.0

open class PartyUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) {
    open suspend fun getMaxInventoryWeight(partyId: Long): Double {
        val partyWithMembers = partyRepository.getPartyWithMembers(partyId)
            ?: return BASE_WEIGHT

        val totalStrength = partyWithMembers.members.sumOf { it.stats.strength }
        return BASE_WEIGHT + (totalStrength * STRENGTH_WEIGHT_RATIO)
    }
}