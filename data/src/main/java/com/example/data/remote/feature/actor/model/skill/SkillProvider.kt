package com.example.data.remote.feature.actor.model.skill

import android.content.Context
import com.example.data.remote.feature.ACTOR_ASSET_ROUTE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SkillProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val skills: List<SkillEntity> by lazy {
        parseSkills()
    }

    private fun parseSkills(): List<SkillEntity> {
        val jsonString =
            context.assets.open("${ACTOR_ASSET_ROUTE}/Skills.json").bufferedReader()
                .use { it.readText() }
        val json = Json { ignoreUnknownKeys = true }
        return json.decodeFromString<List<SkillEntity>>(jsonString)
    }

    fun getAllSkills(): List<SkillEntity> {
        return skills
    }

    fun getSkill(id: Long): Result<SkillEntity> {
        return skills.find { it.skillId == id }?.let {
            Result.success(it)
        } ?: Result.failure(NoSuchElementException("Skill with id $id not found"))
    }
}
