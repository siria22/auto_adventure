package com.example.domain.repository.feature.actor

import com.example.domain.model.feature.actor.base.Personality

interface PersonalityRepository {
    suspend fun getPersonalityList(): List<Personality>
    suspend fun getPersonalityById(personalityId: Long): Result<Personality>
}
