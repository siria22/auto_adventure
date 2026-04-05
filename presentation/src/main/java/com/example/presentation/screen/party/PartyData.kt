package com.example.presentation.screen.party

import com.example.domain.model.feature.actor.base.BaseActor
import com.example.domain.model.feature.party.PartyDetail

data class PartyData(
    val partyDetail: PartyDetail? = null,
    val availableActors: List<BaseActor> = emptyList()
) {
    companion object {
        fun empty() = PartyData(partyDetail = null)
    }
}