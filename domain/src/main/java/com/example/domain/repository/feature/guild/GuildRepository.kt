package com.example.domain.repository.feature.guild

interface GuildRepository {

    suspend fun updateGuildName(newName: String)
    suspend fun getGuildName() : String

    suspend fun updateGuildRank(rank: String)
    suspend fun getGuildRank() : String

    suspend fun setGuildExp(value: Long)
    suspend fun updateGuildExp(amount: Long)
    suspend fun getGuildExp() : Long

    suspend fun setGold(value: Long)
    suspend fun updateGold(amount: Long)
    suspend fun getGold() : Long
}