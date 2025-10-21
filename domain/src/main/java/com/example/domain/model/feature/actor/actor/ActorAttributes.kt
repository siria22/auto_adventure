package com.example.domain.model.feature.actor.actor

data class ActorAttributes(
    val maxHp: Long,     // 최대 체력
    val attack: Long,    // 공격
    val speed: Long,     // 속도
    val evasion: Long,   // 회피
    val magic: Long,     // 마력
    val maxMp: Long,     // 최대 MP
    val fortune: Long,   // 행운
    val accuracy: Long,  // 명중
    val def: Long,        // 방어
    val criticalDamage: Double // 치명타 배율
)