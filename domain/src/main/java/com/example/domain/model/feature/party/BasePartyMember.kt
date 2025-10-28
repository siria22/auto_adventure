package com.example.domain.model.feature.party

import com.example.domain.model.feature.types.PartyPosition

data class BasePartyMember(
    val characterId: Long,
    val partyId: Long,
    val isPartyLeader: Boolean,
    val position: PartyPosition
)
