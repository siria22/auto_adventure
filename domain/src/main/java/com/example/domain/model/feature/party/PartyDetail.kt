package com.example.domain.model.feature.party

import com.example.domain.model.feature.types.PartyPosition

data class PartyDetail(
    val party: Party,
    val members: List<PartyMemberDetail>
)

data class PartyMemberDetail(
    val characterId: Long,
    val name: String,
    val jobName: String,
    val level: Long,
    val currentHp: Long,
    val maxHp: Long,
    val currentMp: Long,
    val maxMp: Long,
    val position: PartyPosition,
    val slotIndex: Int,
    val isLeader: Boolean
)