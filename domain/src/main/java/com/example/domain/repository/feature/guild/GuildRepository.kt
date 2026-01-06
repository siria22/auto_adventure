package com.example.domain.repository.feature.guild

import com.example.domain.scripts.guild.GuildRank
import kotlinx.coroutines.flow.Flow

interface GuildRepository {

    suspend fun updateGuildName(newName: String)
    suspend fun getGuildName(): String

    suspend fun setGuildExp(value: Long)
    suspend fun updateGuildExp(amount: Long)
    suspend fun getGuildTotalExp(): Long

    suspend fun updateGuildRank(rank: GuildRank)
    suspend fun getGuildRank(): GuildRank

    suspend fun setGold(value: Long)
    suspend fun updateGold(amount: Long)
    suspend fun getGold(): Long

    fun observeGold(): Flow<Long>
}