package com.example.presentation.screen.home

import com.example.domain.model.feature.guild.GuildInfoData

data class HomeData(
    val guildInfoData: GuildInfoData,
    val guildMoney: Long
) {
    companion object {
        fun empty() = HomeData(
            guildInfoData = GuildInfoData.mock(),
            guildMoney = 0
        )
    }
}
