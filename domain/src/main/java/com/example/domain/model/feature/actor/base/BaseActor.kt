package com.example.domain.model.feature.actor.base

import java.util.Date

data class BaseActor(
    val id: Long,
    val name: String,
    val totalExp: Long,
    val maxHp: Long,
    val currentHp: Long,
    val maxMp: Long,
    val currentMp: Long,
    val strength: Long,
    val agility: Long,
    val luck: Long,
    val intelligence: Long,
    val remainSkillPoint: Long,
    val remainStatPoint: Long,
    val personalityId: Long, // TODO: Personality 모델과 연결
    val actorCategory: String, // TODO: Enum으로 변경
    val jobId: Long, // TODO: Job 모델과 연결
    val race: String, // TODO: Enum으로 변경
    val elementType: String, // TODO: Enum으로 변경
    val rank: String,
    val adventureCount: Long,
    val killCount: Long,
    val recoveryEndTime: Date?,
    val recruitedAt: Date?
)
