package com.example.domain.model.feature.party

data class PartyMember(
    val characterId: Long,
    val partyId: Long,
    val isPartyLeader: Boolean,
    val position: String // TODO: Enum으로 변경 (FRONT, BACK)
)
