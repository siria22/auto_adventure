package com.example.data.remote.feature.quest.model

import android.content.Context
import com.example.data.remote.feature.QUEST_ASSET_ROUTE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class QuestProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val quests: List<QuestEntity> by lazy {
        parseQuests()
    }

    private fun parseQuests(): List<QuestEntity> {
        val jsonString = context.assets.open("${QUEST_ASSET_ROUTE}/Quests.json").bufferedReader()
            .use { it.readText() }
        val json = Json { ignoreUnknownKeys = true }
        return json.decodeFromString<List<QuestEntity>>(jsonString)
    }

    fun getQuest(id: Long): Result<QuestEntity> {
        return quests.find { it.questId == id }?.let {
            Result.success(it)
        } ?: Result.failure(NoSuchElementException("Quest with id $id not found"))
    }
}
