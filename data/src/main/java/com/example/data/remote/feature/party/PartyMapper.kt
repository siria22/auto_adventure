package com.example.data.remote.feature.party

import com.example.data.remote.feature.party.entity.PartyActionEntity
import com.example.data.remote.feature.party.entity.PartyEntity
import com.example.data.remote.feature.party.entity.PartyMemberEntity
import com.example.domain.model.feature.party.BaseParty
import com.example.domain.model.feature.party.BasePartyAction
import com.example.domain.model.feature.party.BasePartyMember
import com.example.domain.model.feature.types.PartyPosition

fun PartyEntity.toDomain(): BaseParty {
    return BaseParty(
        id = this.partyId,
        name = this.name,
        isOnAdventure = this.isOnAdventure,
        adventureStartTime = this.adventureStartTime
    )
}

fun BaseParty.toEntity(): PartyEntity {
    return PartyEntity(
        partyId = this.id,
        name = this.name,
        isOnAdventure = this.isOnAdventure,
        adventureStartTime = this.adventureStartTime
    )
}

fun PartyActionEntity.toDomain(): BasePartyAction {
    return BasePartyAction(
        partyId = this.partyId,
        index = this.index,
        instruction = this.instruction,
        isActivated = this.isActivated
    )
}

fun BasePartyAction.toEntity(): PartyActionEntity {
    return PartyActionEntity(
        partyId = this.partyId,
        index = this.index,
        instruction = this.instruction,
        isActivated = this.isActivated
    )
}

fun PartyMemberEntity.toDomain(): BasePartyMember {
    return BasePartyMember(
        characterId = this.actorId,
        partyId = this.partyId,
        isPartyLeader = this.isPartyLeader,
        position = PartyPosition.valueOf(this.position)
    )
}

fun BasePartyMember.toEntity(): PartyMemberEntity{
    return PartyMemberEntity(
        actorId = this.characterId,
        partyId = this.partyId,
        isPartyLeader = this.isPartyLeader,
        position = this.position.name
    )
}
