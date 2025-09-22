package com.example.data.common.di.preference

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferenceProvider(
    private val context: Context
) {
    private val Context.dataStore by preferencesDataStore("user_preferences")

    suspend fun updateExamplePreference(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[examplePreference] = value
        }
    }

    fun observeExamplePreference(): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[examplePreference] ?: false
        }
    }


    companion object {
        val examplePreference = booleanPreferencesKey("example_preference")
    }
}