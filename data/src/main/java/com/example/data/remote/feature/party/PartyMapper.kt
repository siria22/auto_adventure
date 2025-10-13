package com.example.data.remote.feature.party

import com.example.data.remote.feature.party.entity.PartyActionEntity
import com.example.data.remote.feature.party.entity.PartyEntity
import com.example.data.remote.feature.party.entity.PartyMemberEntity
import com.example.domain.model.feature.party.Party
import com.example.domain.model.feature.party.PartyAction
import com.example.domain.model.feature.party.PartyMember

fun PartyEntity.toDomain(): Party {
    return Party(
        id = this.partyId,
        name = this.name,
        isOnAdventure = this.isOnAdventure,
        adventureStartTime = this.adventureStartTime
    )
}

fun Party.toEntity(): PartyEntity {
    return PartyEntity(
        partyId = this.id,
        name = this.name,
        isOnAdventure = this.isOnAdventure,
        adventureStartTime = this.adventureStartTime
    )
}

fun PartyActionEntity.toDomain(): PartyAction {
    return PartyAction(
        partyId = this.partyId,
        index = this.index,
        instruction = this.instruction,
        isActivated = this.isActivated
    )
}

fun PartyAction.toEntity(): PartyActionEntity {
    return PartyActionEntity(
        partyId = this.partyId,
        index = this.index,
        instruction = this.instruction,
        isActivated = this.isActivated
    )
}

fun PartyMemberEntity.toDomain(): PartyMember {
    return PartyMember(
        characterId = this.actorId,
        partyId = this.partyId,
        isPartyLeader = this.isPartyLeader,
        position = this.position
    )
}

fun PartyMember.toEntity(): PartyMemberEntity{
    return PartyMemberEntity(
        actorId = this.characterId,
        partyId = this.partyId,
        isPartyLeader = this.isPartyLeader,
        position = this.position
    )
}
