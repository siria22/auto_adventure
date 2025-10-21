package com.example.domain.model.feature.guild

import com.example.domain.scripts.guild.GuildRank

data class GuildInfoData(
    val guildName: String,
    val guildRank: GuildRank,
    val guildTotalExp: Long,
    val guildLevel: Long,
    val expForNextLevel: Long,
    val guildMemberCount: Int,
    /* TODO : 대표 길드원 */
) {
    companion object {
        fun empty() = GuildInfoData(
            guildName = "",
            guildLevel = 0L,
            guildRank = GuildRank.E0,
            guildTotalExp = 0L,
            expForNextLevel = 0L,
            guildMemberCount = 0
        )

        fun mock() = GuildInfoData(
            guildName = "Example_Guild",
            guildLevel = 1L,
            guildRank = GuildRank.E4,
            guildTotalExp = 150L,
            expForNextLevel = 176L,
            guildMemberCount = 3
        )
    }
}
