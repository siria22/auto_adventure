package com.example.domain.model.feature.party

data class BasePartyAction(
    val partyId: Long,
    val index: Long,
    val instruction: String,
    val isActivated: Boolean
)
