package com.example.domain.model.feature.adv

import java.util.Date

data class Party(
    val partyId: Long,
    val partyName: String,
    val isOnAdventure: Boolean,
    val adventureStartTime: Date?,

    val partyMembers: List<BattleEntity>,
    val partyActions: List<PartyAction>
)
