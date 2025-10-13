package com.example.data.remote.feature.actor.repository

import com.example.data.remote.feature.actor.model.skill.SkillProvider
import com.example.data.remote.feature.actor.toDomain
import com.example.domain.model.feature.actor.base.BaseSkill
import com.example.domain.repository.feature.actor.SkillRepository
import javax.inject.Inject

class SkillRepositoryImpl @Inject constructor(
    private val skillProvider: SkillProvider
) : SkillRepository {
    override suspend fun getSkillList(): List<BaseSkill> {
        return skillProvider.getAllSkills().map { it.toDomain() }
    }

    override suspend fun getSkillById(skillId: Long): Result<BaseSkill> {
        return skillProvider.getSkill(skillId).map { it.toDomain() }
    }
}
