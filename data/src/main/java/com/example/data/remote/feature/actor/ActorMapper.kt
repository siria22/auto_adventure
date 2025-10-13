package com.example.data.remote.feature.actor

import com.example.data.remote.feature.actor.entity.ActorActionEntity
import com.example.data.remote.feature.actor.entity.ActorEntity
import com.example.data.remote.feature.actor.entity.ActorSkillEntity
import com.example.data.remote.feature.actor.model.job.JobEntity
import com.example.data.remote.feature.actor.model.personality.PersonalityEntity
import com.example.data.remote.feature.actor.model.skill.SkillEntity
import com.example.domain.model.feature.actor.base.ActorSkillMappingInfo
import com.example.domain.model.feature.actor.base.BaseActor
import com.example.domain.model.feature.actor.base.BaseActorAction
import com.example.domain.model.feature.actor.base.BaseSkill
import com.example.domain.model.feature.actor.base.Job
import com.example.domain.model.feature.actor.base.Personality
import com.example.domain.model.feature.types.ActivationType
import com.example.domain.model.feature.types.SkillElementType
import com.example.domain.model.feature.types.StatType

fun JobEntity.toDomain(): Job {
    return Job(
        jobId = this.jobId,
        nameKor = this.nameKor,
        nameEng = this.nameEng,
        description = this.description
    )
}

fun SkillEntity.toDomain(): BaseSkill {
    return BaseSkill(
        id = this.skillId,
        name = this.name,
        description = this.description,
        maxLevel = this.maxSkillLevel,
        elementType = SkillElementType.valueOf(this.skillElementType),
        activationType = ActivationType.valueOf(this.activationType),
        requiredMp = this.mpUsage,
        multiplier = this.multiplier,
        hitRate = this.hitRate,
        prerequisiteSkillIds = this.prerequisiteSkills
    )
}

fun ActorEntity.toDomain(): BaseActor {
    return BaseActor(
        id = this.actorId,
        name = this.name,
        totalExp = this.totalExp,
        maxHp = this.maxHp,
        currentHp = this.currentHp,
        maxMp = this.maxMp,
        currentMp = this.currentMp,
        strength = this.strength,
        agility = this.agility,
        luck = this.luck,
        intelligence = this.intelligence,
        remainSkillPoint = this.remainSkillPoint,
        remainStatPoint = this.remainStatPoint,
        personalityId = this.personalityId,
        actorCategory = this.actorCategory,
        jobId = this.jobId,
        race = this.race,
        elementType = this.elementType,
        rank = this.rank,
        adventureCount = this.adventureCount,
        killCount = this.killCount,
        recoveryEndTime = this.recoveryEndTime,
        recruitedAt = this.recruitedAt
    )
}

fun BaseActor.toEntity(): ActorEntity {
    return ActorEntity (
        actorId = this.id,
        name = this.name,
        totalExp = this.totalExp,
        maxHp = this.maxHp,
        currentHp = this.currentHp,
        maxMp = this.maxMp,
        currentMp = this.currentMp,
        strength = this.strength,
        agility = this.agility,
        luck = this.luck,
        intelligence = this.intelligence,
        remainSkillPoint = this.remainSkillPoint,
        remainStatPoint = this.remainStatPoint,
        personalityId = this.personalityId,
        actorCategory = this.actorCategory,
        jobId = this.jobId,
        race = this.race,
        elementType = this.elementType,
        rank = this.rank,
        adventureCount = this.adventureCount,
        killCount = this.killCount,
        recoveryEndTime = this.recoveryEndTime,
        recruitedAt = this.recruitedAt
    )
}

fun ActorActionEntity.toDomain(): BaseActorAction {
    return BaseActorAction(
        characterId = this.actorId,
        index = this.index,
        instruction = this.instruction,
        isActivated = this.isActivated
    )
}

fun BaseActorAction.toEntity(): ActorActionEntity {
    return ActorActionEntity(
        actorId = this.characterId,
        index = this.index,
        instruction = this.instruction,
        isActivated = this.isActivated
    )
}

fun ActorSkillEntity.toDomain(): ActorSkillMappingInfo {
    return ActorSkillMappingInfo(
        actorId = this.actorId,
        skillId = this.skillId,
        skillLevel = this.skillLevel,
        isEquipped = this.isEquipped
    )
}

fun ActorSkillMappingInfo.toEntity(): ActorSkillEntity {
    return ActorSkillEntity(
        actorId = this.actorId,
        skillId = this.skillId,
        skillLevel = this.skillLevel,
        isEquipped = this.isEquipped,
    )
}

fun PersonalityEntity.toDomain(): Personality {
    return Personality(
        id = this.personalityId,
        name = this.name,
        increaseTarget = StatType.valueOf(this.increaseTarget),
        increaseAmount = this.increaseAmount,
        decreaseTarget = StatType.valueOf(this.decreaseTarget),
        decreaseAmount = this.decreaseAmount
    )
}