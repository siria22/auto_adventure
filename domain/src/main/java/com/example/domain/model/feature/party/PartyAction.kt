package com.example.domain.model.feature.party

data class PartyAction(
    val partyId: Long,
    val index: Long,
    val instruction: String,
    val isActivated: Boolean
)
