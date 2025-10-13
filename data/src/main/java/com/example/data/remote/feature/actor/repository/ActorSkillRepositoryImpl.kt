package com.example.data.remote.feature.actor.repository

import com.example.data.remote.feature.actor.dao.ActorSkillDao
import com.example.data.remote.feature.actor.toDomain
import com.example.data.remote.feature.actor.toEntity
import com.example.domain.model.feature.actor.base.ActorSkillMappingInfo
import com.example.domain.repository.feature.actor.ActorSkillRepository
import javax.inject.Inject

class ActorSkillRepositoryImpl @Inject constructor(
    private val actorSkillDao: ActorSkillDao
) : ActorSkillRepository {
    override suspend fun insertSkill(skillMappingInfo: ActorSkillMappingInfo) {
        actorSkillDao.insert(skillMappingInfo.toEntity())
    }

    override suspend fun getSkillsByActorId(characterId: Long): List<ActorSkillMappingInfo> {
        return actorSkillDao.getSkillsByActorId(characterId).map { it.toDomain() }
    }
}
