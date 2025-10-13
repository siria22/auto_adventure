package com.example.domain.repository.feature.actor

import com.example.domain.model.feature.actor.base.ActorSkillMappingInfo

interface ActorSkillRepository {
    suspend fun insertSkill(skillMappingInfo: ActorSkillMappingInfo)
    suspend fun getSkillsByActorId(characterId: Long): List<ActorSkillMappingInfo>
}