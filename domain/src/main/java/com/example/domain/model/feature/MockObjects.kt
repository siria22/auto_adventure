package com.example.domain.model.feature


import com.example.domain.model.feature.actor.actor.Actor
import com.example.domain.model.feature.actor.actor.ActorEquips
import com.example.domain.model.feature.actor.base.BaseActor
import com.example.domain.model.feature.actor.base.Job
import com.example.domain.model.feature.actor.base.Personality
import com.example.domain.model.feature.types.StatType

internal val mockBaseActor = BaseActor(
    id = 0L,
    name = "Sairo",
    totalExp = 22,
    maxHp = 120,
    currentHp = 120,
    maxMp = 100,
    currentMp = 100,
    strength = 8,
    agility = 4,
    luck = 3,
    intelligence = 4,
    remainSkillPoint = 0,
    remainStatPoint = 1,
    personalityId = 1,
    actorCategory = "HERO",
    jobId = 1,
    race = "HUMAN",
    elementType = "NORMAL",
    rank = "S",
    adventureCount = 10,
    killCount = 173,
    recoveryEndTime = null,
    recruitedAt = null
)

internal val mockJob = Job(
    jobId = 1L,
    nameKor = "전사",
    nameEng = "Warrior",
    description = "전사입니다."
)

internal val mockPersonality = Personality(
    id = 1L,
    name = "용감함",
    increaseTarget = StatType.STRENGTH,
    increaseAmount = 1,
    decreaseTarget = StatType.AGILITY,
    decreaseAmount = 1
)

internal val mockActorEquips = ActorEquips(
    weapon = null,
    sidearm = null,
    armor = null,
    accessory = null,
    gloves = null,
    shoes = null
)

val mockActor = Actor.create(
    baseActor = mockBaseActor,
    job = mockJob,
    personality = mockPersonality,
    actorEquips = mockActorEquips,
    actorSkills = emptyList(),
    actorActions = emptyList()
)
