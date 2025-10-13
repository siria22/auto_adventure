package com.example.data.remote.feature.guild.repository

import com.example.data.remote.feature.guild.dao.GuildPreferenceProvider
import com.example.domain.repository.feature.guild.GuildRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GuildRepositoryImpl @Inject constructor(
    private val guildPreferenceProvider: GuildPreferenceProvider
) : GuildRepository {
    override suspend fun updateGuildName(newName: String) {
        guildPreferenceProvider.updateGuildName(newName)
    }

    override suspend fun getGuildName(): String {
        return guildPreferenceProvider.observeGuildName().first()
    }

    override suspend fun updateGuildRank(rank: String) {
        guildPreferenceProvider.updateGuildRank(rank)
    }

    override suspend fun getGuildRank(): String {
        return guildPreferenceProvider.observeGuildRank().first()
    }

    override suspend fun setGuildExp(value: Long) {
        guildPreferenceProvider.setGuildExp(value)
    }

    override suspend fun updateGuildExp(amount: Long) {
        guildPreferenceProvider.updateGuildExp(amount)
    }

    override suspend fun getGuildExp(): Long {
        return guildPreferenceProvider.observeGuildExp().first()
    }

    override suspend fun setGold(value: Long) {
        guildPreferenceProvider.setGold(value)
    }

    override suspend fun updateGold(amount: Long) {
        guildPreferenceProvider.updateGold(amount)
    }

    override suspend fun getGold(): Long {
        return guildPreferenceProvider.observeGold().first()
    }
}
