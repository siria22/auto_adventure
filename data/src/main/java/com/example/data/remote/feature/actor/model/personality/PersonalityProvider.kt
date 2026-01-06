package com.example.data.remote.feature.actor.model.personality

import android.content.Context
import com.example.data.remote.feature.ACTOR_ASSET_ROUTE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class PersonalityProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val personality: List<PersonalityEntity> by lazy {
        parsePersonalities()
    }

    private fun parsePersonalities(): List<PersonalityEntity> {
        val jsonString =
            context.assets.open("${ACTOR_ASSET_ROUTE}/Personalities.json").bufferedReader()
                .use { it.readText() }
        val json = Json { ignoreUnknownKeys = true }
        return json.decodeFromString<List<PersonalityEntity>>(jsonString)
    }

    fun getPersonalities(): List<PersonalityEntity> {
        return personality
    }

    fun getPersonality(id: Long): Result<PersonalityEntity> {
        return personality.find { it.personalityId == id }?.let {
            Result.success(it)
        } ?: Result.failure(NoSuchElementException("Personality with id $id not found"))
    }
}
