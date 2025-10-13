package com.example.data.remote.feature.actor.repository

import com.example.data.remote.feature.actor.model.personality.PersonalityProvider
import com.example.data.remote.feature.actor.toDomain
import com.example.domain.model.feature.actor.base.Personality
import com.example.domain.repository.feature.actor.PersonalityRepository
import javax.inject.Inject

class PersonalityRepositoryImpl @Inject constructor(
    private val personalityProvider: PersonalityProvider
) : PersonalityRepository {
    override suspend fun getPersonalityList(): List<Personality> {
        return personalityProvider.getPersonalities().map { it.toDomain() }
    }

    override suspend fun getPersonalityById(personalityId: Long): Result<Personality> {
        return personalityProvider.getPersonality(personalityId).map { it.toDomain() }
    }
}
