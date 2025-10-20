package com.example.data.remote.feature.guild.dao

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GuildPreferenceProvider(
    private val context: Context
) {
    private val Context.dataStore by preferencesDataStore("user_preferences")


    suspend fun updateGuildName(value: String) {
        context.dataStore.edit { preferences ->
            preferences[guildName] = value
        }
    }

    fun observeGuildName(): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[guildName] ?: "My Guild"
        }
    }

    suspend fun updateGuildRank(value: String) {
        context.dataStore.edit { preferences ->
            preferences[guildRank] = value
        }
    }

    fun observeGuildRank(): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[guildRank] ?: ""
        }
    }

    suspend fun setGuildExp(value: Long){
        context.dataStore.edit { prefs ->
            prefs[guildExp] = value
        }
    }

    suspend fun updateGuildExp(amount: Long) {
        context.dataStore.edit { preferences ->
            val current = preferences[guildExp] ?: throw IllegalArgumentException("Failed to fetch guild exp")
            preferences[guildExp] = current + amount
        }
    }

    fun observeGuildExp(): Flow<Long> {
        return context.dataStore.data.map { prefs ->
            prefs[guildExp] ?: 0L
        }
    }

    suspend fun setGold(value: Long) {
        context.dataStore.edit { prefs ->
            prefs[gold] = value
        }
    }

    suspend fun updateGold(value: Long) {
        context.dataStore.edit { prefs ->
            val current = prefs[gold] ?: throw IllegalArgumentException("Failed to fetch gold")
            prefs[gold] = current + value
        }
    }

    fun observeGold(): Flow<Long> {
        return context.dataStore.data.map { prefs ->
            prefs[gold] ?: 0L
        }
    }

    companion object {
        val guildName = stringPreferencesKey("guild_name")
        val guildRank = stringPreferencesKey("guild_rank")
        val guildExp = longPreferencesKey("guild_exp")
        val gold = longPreferencesKey("gold")
    }
}