package com.example.domain.repository.feature.actor

import com.example.domain.model.feature.actor.base.BaseSkill

interface SkillRepository {

    suspend fun getSkillList(): List<BaseSkill>

    suspend fun getSkillById(skillId: Long): Result<BaseSkill>

}