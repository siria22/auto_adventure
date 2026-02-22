package com.example.domain.model.feature.party

import com.example.domain.model.feature.types.PartyPosition

data class PartyMember(
    val characterId: Long,
    val partyId: Long,
    val isPartyLeader: Boolean,
    val position: PartyPosition,
    val slotIndex: Int
)
